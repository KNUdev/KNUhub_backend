package ua.knu.knudev.knuhubeducation.service;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;
import ua.knu.knudev.fileserviceapi.api.ImageServiceApi;
import ua.knu.knudev.fileserviceapi.subfolder.ImageSubfolder;
import ua.knu.knudev.knuhubcommon.constant.OptionQuestionType;
import ua.knu.knudev.knuhubeducation.domain.Image;
import ua.knu.knudev.knuhubeducation.domain.Option;
import ua.knu.knudev.knuhubeducation.domain.OptionQuestion;
import ua.knu.knudev.knuhubeducation.domain.TestDomain;
import ua.knu.knudev.knuhubeducation.mapper.OptionQuestionLiteMapper;
import ua.knu.knudev.knuhubeducation.repository.OptionQuestionRepository;
import ua.knu.knudev.knuhubeducation.repository.TestRepository;
import ua.knu.knudev.knuhubeducationapi.api.OptionQuestionApi;
import ua.knu.knudev.knuhubeducationapi.dto.OptionQuestionLiteDto;
import ua.knu.knudev.knuhubeducationapi.exception.OptionQuestionException;
import ua.knu.knudev.knuhubeducationapi.exception.TestException;
import ua.knu.knudev.knuhubeducationapi.request.OptionCreationRequest;
import ua.knu.knudev.knuhubeducationapi.request.OptionQuestionCreationRequest;
import ua.knu.knudev.knuhubeducationapi.request.OptionQuestionUpdateRequest;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static ua.knu.knudev.knuhubcommon.service.HelperService.getOrDefault;
import static ua.knu.knudev.knuhubeducation.service.EducationHelperService.getImagesFilenames;

@Service
@RequiredArgsConstructor
@Validated
@Slf4j
public class OptionQuestionService implements OptionQuestionApi {

    private final OptionQuestionRepository optionQuestionRepository;
    private final ImageServiceApi imageServiceApi;
    private final TestRepository testRepository;
    private final OptionQuestionLiteMapper optionQuestionLiteMapper;

    @Transactional
    public OptionQuestionLiteDto create(@Valid OptionQuestionCreationRequest request) {
        validateCreationRequest(request);

        TestDomain test = testRepository.findById(request.testId())
                .orElseThrow(() -> new TestException("Can`t create option question. Test with id " + request.testId() + " is not found"));

        Set<String> uploadedImages = new HashSet<>();
        try {
            Set<Option> options = createOptions(request.options(), uploadedImages);

            OptionQuestion optionQuestion = new OptionQuestion();
            optionQuestion.setText(request.text());
            optionQuestion.setType(request.questionType());
            optionQuestion.setTest(test);
            optionQuestion.addOptions(options);
            optionQuestion.setMaxMark(request.maxMark() == null ? BigDecimal.ONE : request.maxMark());
            if (request.images() != null && !request.images().isEmpty()) {
                createQuestionImages(optionQuestion, request.images(), uploadedImages);
            }

            OptionQuestion response = optionQuestionRepository.save(optionQuestion);
            return optionQuestionLiteMapper.toDto(response);
        } catch (Exception e) {
            removeImages(uploadedImages);
            throw e;
        }
    }

    @Transactional
    public OptionQuestionLiteDto update(@Valid OptionQuestionUpdateRequest request) {
        OptionQuestion optionQuestion = optionQuestionRepository.findById(request.questionId())
                .orElseThrow(() -> new OptionQuestionException("Can`t update non-existent option question"));

        if (request.testId() != null) {
            TestDomain test = testRepository.findById(request.testId())
                    .orElseThrow(() -> new TestException("Can`t update option question. Test with id " + request.testId() + " is not found"));
            optionQuestion.setTest(test);
        }
        optionQuestion.setText(getOrDefault(request.text(), optionQuestion.getText()));
        optionQuestion.setType(getOrDefault(request.questionType(), optionQuestion.getType()));
        optionQuestion.setMaxMark(getOrDefault(request.maxMark(), optionQuestion.getMaxMark()));

        Set<String> uploadedImages = new HashSet<>();
        OptionQuestion response;
        try {
            Set<String> previousImages = new HashSet<>();

            if (request.options() != null) {
                updateOptions(request, optionQuestion, uploadedImages, previousImages);
            }
            if (request.images() != null) {
                updateQuestionImages(request, optionQuestion, uploadedImages, previousImages);
            }

            response = optionQuestionRepository.save(optionQuestion);
            removeImages(previousImages);
        } catch (Exception e) {
            removeImages(uploadedImages);
            throw e;
        }

        return optionQuestionLiteMapper.toDto(response);
    }

    private void validateCreationRequest(OptionQuestionCreationRequest request) {
        if (request.text() == null && (request.images() == null || request.images().isEmpty())) {
            throw new OptionQuestionException("Can not create question. Text is empty and images has 0 length");
        }

        validateOptions(request.options(), request.questionType());
    }

    private void validateOptions(Set<OptionCreationRequest> options, OptionQuestionType questionType) {
        long correctOptions = options.stream()
                .filter(OptionCreationRequest::isCorrect)
                .count();
        if (questionType == OptionQuestionType.ONE_ANSWER && correctOptions != 1) {
            throw new OptionQuestionException("ONE_ANSWER questio must have only one correct option");
        }
        if (questionType == OptionQuestionType.MULTI_ANSWER && correctOptions == 0) {
            throw new OptionQuestionException("MULTI_ANSWER question must have at least one correct option");
        }
    }

    private Set<Option> createOptions(Set<OptionCreationRequest> requests, Set<String> uploadedImages) {
        Set<Option> options = new HashSet<>();

        for (OptionCreationRequest optionCreationRequest : requests) {
            Option option = new Option();
            option.setText(optionCreationRequest.text());
            option.setIsCorrect(optionCreationRequest.isCorrect());
            if (optionCreationRequest.image() != null) {
                String imageFilename = imageServiceApi.uploadFile(optionCreationRequest.image(), ImageSubfolder.EDUCATION_TEST);
                uploadedImages.add(imageFilename);
                Image image = Image.builder().filename(imageFilename).build();
                option.setImage(image);
            }

            options.add(option);
        }

        return options;
    }

    private void updateOptions(OptionQuestionUpdateRequest request, OptionQuestion existingQuestion, Set<String> uploadedImages, Set<String> previousImages) {
        validateOptions(request.options(), getOrDefault(request.questionType(), existingQuestion.getType()));
        previousImages.addAll(existingQuestion.getOptions().stream()
                .map(s -> s.getImage().getFilename())
                .collect(Collectors.toSet()));
        Set<Option> options = createOptions(request.options(), uploadedImages);
        existingQuestion.removeAllOptions();
        existingQuestion.addOptions(options);
    }

    private void createQuestionImages(OptionQuestion optionQuestion, Set<MultipartFile> imagesFiles, Set<String> uploadedImages) {
        Set<String> imageFilenames = uploadImages(imagesFiles);
        uploadedImages.addAll(imageFilenames);
        Set<Image> images = imageFilenames.stream()
                .map(filename -> Image.builder().filename(filename).build())
                .collect(Collectors.toSet());
        optionQuestion.addImages(images);
    }

    private void updateQuestionImages(OptionQuestionUpdateRequest request, OptionQuestion existingQuestion, Set<String> uploadedImages, Set<String> previousImages) {
        previousImages.addAll(getImagesFilenames(existingQuestion.getImages()));
        createQuestionImages(existingQuestion, request.images(), uploadedImages);
    }

    private Set<String> uploadImages(Set<MultipartFile> images) {
        if (images == null || images.isEmpty()) {
            return new HashSet<>();
        }

        Set<String> imageFilenames = new HashSet<>();

        try {
            for (MultipartFile image : images) {
                String filename = imageServiceApi.uploadFile(image, ImageSubfolder.EDUCATION_TEST);
                imageFilenames.add(filename);
            }
        } catch (Exception e) {
            removeImages(imageFilenames);

            throw e;
        }

        return imageFilenames;
    }

    private void removeImages(Set<String> imageFilenames) {
        for (String filename : imageFilenames) {
            removeImage(filename);
        }
    }

    private void removeImage(String filename) {
        try {
            imageServiceApi.removeByFilename(filename, ImageSubfolder.EDUCATION_TEST);
        } catch (Exception e) {
            log.warn(e.getMessage());
        }
    }
}
