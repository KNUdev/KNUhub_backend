package ua.knu.knudev.knuhubeducation.service;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;
import ua.knu.knudev.knuhubeducation.domain.Image;
import ua.knu.knudev.knuhubeducation.domain.TestDomain;
import ua.knu.knudev.knuhubeducation.domain.TextQuestion;
import ua.knu.knudev.knuhubeducation.mapper.TextQuestionLiteMapper;
import ua.knu.knudev.knuhubeducation.repository.TestRepository;
import ua.knu.knudev.knuhubeducation.repository.TextQuestionRepository;
import ua.knu.knudev.knuhubeducationapi.api.EducationImageServiceApi;
import ua.knu.knudev.knuhubeducationapi.api.TextQuestionApi;
import ua.knu.knudev.knuhubeducationapi.dto.TextQuestionLiteDto;
import ua.knu.knudev.knuhubeducationapi.exception.MatchQuestionException;
import ua.knu.knudev.knuhubeducationapi.exception.TestException;
import ua.knu.knudev.knuhubeducationapi.exception.TextQuestionException;
import ua.knu.knudev.knuhubeducationapi.request.TextQuestionCreationRequest;
import ua.knu.knudev.knuhubeducationapi.request.TextQuestionUpdateRequest;

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
public class TextQuestionService implements TextQuestionApi {

    private final TextQuestionRepository textQuestionRepository;
    private final TestRepository testRepository;
    private final EducationImageServiceApi educationImageServiceApi;
    private final TextQuestionLiteMapper textQuestionLiteMapper;


    @Override
    public TextQuestionLiteDto create(@Valid TextQuestionCreationRequest request) {
        validateCreationRequest(request);

        TestDomain test = testRepository.findById(request.testId())
                .orElseThrow(() -> new TestException("Can`t create text question. Test with id " + request.testId() + " is not found"));

        Set<String> uploadedImages = new HashSet<>();
        try {
            TextQuestion textQuestion = new TextQuestion();
            textQuestion.setTest(test);
            textQuestion.setText(request.text());
            textQuestion.setMaxMark(getOrDefault(request.maxMark(), BigDecimal.ONE));
            textQuestion.setCorrectAnswers(request.correctAnswers());
            textQuestion.setIsCaseSensitive(getOrDefault(request.isCaseSensitive(), false));
            if (request.images() != null && !request.images().isEmpty()) {
                addImagesToQuestion(textQuestion, request.images(), uploadedImages);
            }

            TextQuestion response = textQuestionRepository.save(textQuestion);
            log.info("Created textQuestion with id: {}", response.getId());
            return textQuestionLiteMapper.toDto(response);
        } catch (Exception e) {
            educationImageServiceApi.removeImages(uploadedImages);
            throw e;
        }
    }

    @Override
    public TextQuestionLiteDto update(@Valid TextQuestionUpdateRequest request) {
        TextQuestion textQuestion = getTextQuestionById(request.questionId());
        validateMaxMark(request.maxMark());

        if (request.testId() != null) {
            TestDomain test = testRepository.findById(request.testId())
                    .orElseThrow(() -> new TestException("Can`t update text question. Test with id " + request.testId() + " is not found"));
            textQuestion.setTest(test);
        }
        textQuestion.setText(request.text());
        textQuestion.setMaxMark(getOrDefault(request.maxMark(), textQuestion.getMaxMark()));
        textQuestion.setIsCaseSensitive(getOrDefault(request.isCaseSensitive(), textQuestion.getIsCaseSensitive()));

        Set<String> uploadedImages = new HashSet<>();
        try {
            Set<String> previousImages = new HashSet<>();

            if (request.correctAnswers() != null) {
                textQuestion.removeAllCorrectAnswers();
                textQuestion.addCorrectAnswers(request.correctAnswers());
            }

            if (request.images() != null) {
                updateQuestionImages(request, textQuestion, uploadedImages, previousImages);
            }

            TextQuestion response = textQuestionRepository.save(textQuestion);
            log.info("Updated textQuestion with id: {}", response.getId());
            return textQuestionLiteMapper.toDto(response);
        } catch (Exception e) {
            educationImageServiceApi.removeImages(uploadedImages);
            throw e;
        }

    }

    @Override
    public void delete(UUID id) {
        TextQuestion textQuestion = getTextQuestionById(id);

        Set<String> images = getImagesFilenames(textQuestion.getImages());

        textQuestionRepository.deleteById(id);
        log.info("Deleted textQuestion with id: {}", id);
        educationImageServiceApi.removeImages(images);
    }

    @Override
    public TextQuestionLiteDto findById(UUID id) {
        TextQuestion textQuestion = getTextQuestionById(id);

        log.info("Found textQuestion with id: {}", id);
        return textQuestionLiteMapper.toDto(textQuestion);
    }

    private TextQuestion getTextQuestionById(UUID id) {
        return textQuestionRepository.findById(id).orElseThrow(
                () -> new TextQuestionException("TextQuestion with id " + id + " is not found")
        );
    }

    private void addImagesToQuestion(TextQuestion textQuestion, Set<MultipartFile> imagesFiles, Set<String> uploadedImages) {
        Set<String> imageFilenames = educationImageServiceApi.uploadImagesWithCatchRemove(imagesFiles);
        uploadedImages.addAll(imageFilenames);
        Set<Image> images = imageFilenames.stream()
                .map(filename -> Image.builder().filename(filename).build())
                .collect(Collectors.toSet());
        textQuestion.addImages(images);
    }

    private void validateCreationRequest(TextQuestionCreationRequest request) {
        if (request.text() == null && (request.images() == null || request.images().isEmpty())) {
            throw new MatchQuestionException("Can not create question. Text is empty and 'images' field has 0 length");
        }

        validateMaxMark(request.maxMark());
    }

    private void validateMaxMark(BigDecimal maxMark) {
        if (maxMark == null) {
            return;
        }

        int precision = maxMark.precision();
        int scale = maxMark.scale();
        int digitsBeforeDecimal = precision - scale;

        if (digitsBeforeDecimal > 3) {
            throw new TextQuestionException("Max mark can not contain more than 3 digits before the decimal point");
        }
        if (scale > 3) {
            throw new TextQuestionException("Max mark can not contain more than 3 digits after the decimal point");
        }
    }

    private void updateQuestionImages(TextQuestionUpdateRequest request, TextQuestion existingQuestion, Set<String> uploadedImages, Set<String> previousImages) {
        previousImages.addAll(getImagesFilenames(existingQuestion.getImages()));
        existingQuestion.removeAllImages();
        addImagesToQuestion(existingQuestion, request.images(), uploadedImages);
    }
}
