package ua.knu.knudev.knuhubeducation.service;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;
import ua.knu.knudev.knuhubeducation.domain.Image;
import ua.knu.knudev.knuhubeducation.domain.TestDomain;
import ua.knu.knudev.knuhubeducation.domain.matching.MatchQuestion;
import ua.knu.knudev.knuhubeducation.domain.matching.MatchingLeft;
import ua.knu.knudev.knuhubeducation.domain.matching.MatchingPair;
import ua.knu.knudev.knuhubeducation.domain.matching.MatchingRight;
import ua.knu.knudev.knuhubeducation.mapper.MatchQuestionLiteMapper;
import ua.knu.knudev.knuhubeducation.repository.MatchQuestionRepository;
import ua.knu.knudev.knuhubeducation.repository.TestRepository;
import ua.knu.knudev.knuhubeducationapi.api.EducationImageServiceApi;
import ua.knu.knudev.knuhubeducationapi.api.MatchQuestionApi;
import ua.knu.knudev.knuhubeducationapi.dto.MatchQuestionLiteDto;
import ua.knu.knudev.knuhubeducationapi.exception.MatchQuestionException;
import ua.knu.knudev.knuhubeducationapi.exception.TestException;
import ua.knu.knudev.knuhubeducationapi.request.CorrectMatchingPairCreationRequest;
import ua.knu.knudev.knuhubeducationapi.request.MatchQuestionCreationRequest;
import ua.knu.knudev.knuhubeducationapi.request.MatchQuestionUpdateRequest;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static ua.knu.knudev.knuhubcommon.service.HelperService.getOrDefault;
import static ua.knu.knudev.knuhubeducation.service.EducationHelperService.getImagesFilenames;

@Service
@RequiredArgsConstructor
@Validated
@Slf4j
public class MatchQuestionService implements MatchQuestionApi {

    private final MatchQuestionRepository matchQuestionRepository;
    private final TestRepository testRepository;
    private final EducationImageServiceApi educationImageServiceApi;
    private final MatchQuestionLiteMapper matchQuestionLiteMapper;

    @Override
    @Transactional
    public MatchQuestionLiteDto create(@Valid MatchQuestionCreationRequest request) {
        validateCreationRequest(request);

        TestDomain test = testRepository.findById(request.testId())
                .orElseThrow(() -> new TestException("Can`t create match question. Test with id " + request.testId() + " is not found"));

        Set<String> uploadedImages = new HashSet<>();
        try {
            Set<MatchingPair> pairs = createMatchingPairs(request.matchingPairs());

            MatchQuestion question = new MatchQuestion();
            question.setTest(test);
            question.setText(request.text());
            question.setMaxMark(request.maxMark() == null ? BigDecimal.ONE : request.maxMark());
            question.addCorrectMatchingPairs(pairs);
            if (request.images() != null && !request.images().isEmpty()) {
                addImagesToQuestion(question, request.images(), uploadedImages);
            }

            MatchQuestion response = matchQuestionRepository.save(question);
            log.info("Created matchQuestion with id: {}", response.getId());
            return matchQuestionLiteMapper.toDto(response);
        } catch (Exception e) {
            educationImageServiceApi.removeImages(uploadedImages);
            throw e;
        }
    }

    @Override
    @Transactional
    public MatchQuestionLiteDto update(@Valid MatchQuestionUpdateRequest request) {
        MatchQuestion matchQuestion = getMatchQuestionById(request.questionId());
        validateMaxMark(request.maxMark());

        if (request.testId() != null) {
            TestDomain test = testRepository.findById(request.testId())
                    .orElseThrow(() -> new TestException("Can`t update match question. Test with id " + request.testId() + " is not found"));
            matchQuestion.setTest(test);
        }
        matchQuestion.setText(getOrDefault(request.text(), matchQuestion.getText()));
        matchQuestion.setMaxMark(getOrDefault(request.maxMark(), matchQuestion.getMaxMark()));

        Set<String> uploadedImages = new HashSet<>();
        MatchQuestion response;
        try {
            Set<String> previousImages = new HashSet<>();

            if (request.matchingPairs() != null) {
                validateMatchingPairs(request.matchingPairs());
                Set<MatchingPair> pairs = createMatchingPairs(request.matchingPairs());
                matchQuestion.removeAllCorrectMatchingPairs();
                matchQuestion.addCorrectMatchingPairs(pairs);
            }
            if (request.images() != null) {
                updateQuestionImages(request, matchQuestion, uploadedImages, previousImages);
            }

            response = matchQuestionRepository.save(matchQuestion);
            educationImageServiceApi.removeImages(previousImages);
            log.info("Updated matchQuestion with id: {}", response.getId());
            return matchQuestionLiteMapper.toDto(response);
        } catch (Exception e) {
            educationImageServiceApi.removeImages(uploadedImages);
            throw e;
        }
    }

    @Override
    public void delete(UUID id) {
        MatchQuestion matchQuestion = getMatchQuestionById(id);

        Set<String> images = getImagesFilenames(matchQuestion.getImages());

        matchQuestionRepository.deleteById(matchQuestion.getId());
        log.info("Deleted matchQuestion with id: {}", id);
        educationImageServiceApi.removeImages(images);
    }

    @Override
    public MatchQuestionLiteDto findById(UUID id) {
        MatchQuestion matchQuestion = getMatchQuestionById(id);

        log.info("Found matchQuestion with id: {}", id);
        return matchQuestionLiteMapper.toDto(matchQuestion);
    }

    private Set<MatchingPair> createMatchingPairs(Set<CorrectMatchingPairCreationRequest> requests) {
        Set<MatchingPair> pairs = new HashSet<>();

        for (CorrectMatchingPairCreationRequest request : requests) {
            MatchingPair pair = new MatchingPair();
            MatchingLeft left = MatchingLeft.builder()
                    .text(request.matchingLeft())
                    .build();
            MatchingRight right = MatchingRight.builder()
                    .text(request.matchingRight())
                    .build();
            pair.setMatchingLeft(left);
            pair.setMatchingRight(right);

            pairs.add(pair);
        }

        return pairs;
    }

    private MatchQuestion getMatchQuestionById(UUID id) {
        return matchQuestionRepository.findById(id).orElseThrow(
                () -> new MatchQuestionException("MatchQuestion with id " + id + " is not found")
        );
    }

    private void validateCreationRequest(MatchQuestionCreationRequest request) {
        if (request.text() == null && (request.images() == null || request.images().isEmpty())) {
            throw new MatchQuestionException("Can not create question. Text is empty and 'images' field has 0 length");
        }

        validateMaxMark(request.maxMark());
        validateMatchingPairs(request.matchingPairs());
    }

    private void validateMatchingPairs(Set<CorrectMatchingPairCreationRequest> pairs) {
        Set<String> leftAnswers = pairs.stream()
                .map(CorrectMatchingPairCreationRequest::matchingLeft)
                .collect(Collectors.toSet());
        Set<String> rightAnswers = pairs.stream()
                .map(CorrectMatchingPairCreationRequest::matchingRight)
                .collect(Collectors.toSet());

        if (leftAnswers.size() != pairs.size()) {
            throw new MatchQuestionException("Can not create question. Matching pairs' left strings must not contain duplicates");
        }
        if (rightAnswers.size() != pairs.size()) {
            throw new MatchQuestionException("Can not create question. Matching pairs' right strings must not contain duplicates");
        }
    }

    private void validateMaxMark(BigDecimal maxMark) {
        if (maxMark == null) {
            return;
        }

        int precision = maxMark.precision();
        int scale = maxMark.scale();
        int digitsBeforeDecimal = precision - scale;

        if (digitsBeforeDecimal > 3) {
            throw new MatchQuestionException("Max mark can not contain more than 3 digits before the decimal point");
        }
        if (scale > 3) {
            throw new MatchQuestionException("Max mark can not contain more than 3 digits after the decimal point");
        }
    }

    private void addImagesToQuestion(MatchQuestion matchQuestion, Set<MultipartFile> imagesFiles, Set<String> uploadedImages) {
        Set<String> imageFilenames = educationImageServiceApi.uploadImagesWithCatchRemove(imagesFiles);
        uploadedImages.addAll(imageFilenames);
        Set<Image> images = imageFilenames.stream()
                .map(filename -> Image.builder().filename(filename).build())
                .collect(Collectors.toSet());
        matchQuestion.addImages(images);
    }

    private void updateQuestionImages(MatchQuestionUpdateRequest request, MatchQuestion existingQuestion, Set<String> uploadedImages, Set<String> previousImages) {
        previousImages.addAll(getImagesFilenames(existingQuestion.getImages()));
        existingQuestion.removeAllImages();
        addImagesToQuestion(existingQuestion, request.images(), uploadedImages);
    }
}
