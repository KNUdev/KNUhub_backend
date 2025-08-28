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

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

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
            if (!request.images().isEmpty()) {
                Set<String> imageFilenames = uploadImages(request.images());
                uploadedImages.addAll(imageFilenames);
                Set<Image> images = imageFilenames.stream()
                        .map(filename -> Image.builder().filename(filename).build())
                        .collect(Collectors.toSet());
                optionQuestion.setImages(images);
            }

            OptionQuestion response = optionQuestionRepository.save(optionQuestion);
            return optionQuestionLiteMapper.toDto(response);
        } catch (Exception e) {
            removeImages(uploadedImages);
            throw e;
        }
    }

    private void validateCreationRequest(OptionQuestionCreationRequest request) {
        if (request.text() == null && request.images().isEmpty()) {
            throw new OptionQuestionException("Can not create question. Text is empty and images has 0 length");
        }

        long correctOptions = request.options().stream()
                .filter(OptionCreationRequest::isCorrect)
                .count();
        if (request.questionType() == OptionQuestionType.ONE_ANSWER && correctOptions != 1) {
            throw new OptionQuestionException("Can not create ONE_ANSWER question. Only one option can be set as correct");
        }
        if (request.questionType() == OptionQuestionType.MULTI_ANSWER && correctOptions == 0) {
            throw new OptionQuestionException("Can not create MULTI_ANSWER question. Must be at least one correct option");
        }
    }

//    private String uploadImage(MultipartFile image) {
//        try {
//            return imageServiceApi.uploadFile(image, ImageSubfolder.EDUCATION_TEST);
//        } catch (Exception e) {
//            removeImages(imageFilenames);
//            throw e;
//        }
//    }

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
            log.error(e.getMessage());
        }
    }
}
