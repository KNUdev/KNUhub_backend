package ua.knu.knudev.knuhubeducation.service;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;
import ua.knu.knudev.fileserviceapi.api.ImageServiceApi;
import ua.knu.knudev.fileserviceapi.subfolder.ImageSubfolder;
import ua.knu.knudev.knuhubeducation.domain.Image;
import ua.knu.knudev.knuhubeducation.domain.TestDomain;
import ua.knu.knudev.knuhubeducation.mapper.TestMapper;
import ua.knu.knudev.knuhubeducation.repository.TestRepository;
import ua.knu.knudev.knuhubeducationapi.api.TestApi;
import ua.knu.knudev.knuhubeducationapi.dto.TestDto;
import ua.knu.knudev.knuhubeducationapi.exception.TestException;
import ua.knu.knudev.knuhubeducationapi.request.TestCreationRequest;
import ua.knu.knudev.knuhubeducationapi.request.TestUpdateRequest;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static ua.knu.knudev.knuhubeducation.service.HelperService.getOrDefault;

@Service
@RequiredArgsConstructor
@Validated
@Slf4j
public class TestService implements TestApi {

    private final TestRepository testRepository;
    private final ImageServiceApi imageServiceApi;
    private final TestMapper testMapper;

    @Override
    public TestDto createTest(@Valid TestCreationRequest request) {
        Set<String> uploadedImages = uploadImages(request.images());

        try {
            Set<Image> images = uploadedImages.stream()
                    .map(filename -> Image.builder().filename(filename).build())
                    .collect(Collectors.toSet());

            TestDomain test = TestDomain.builder()
                    .title(request.title())
                    .description(request.description())
                    .isProtectedMode(request.isProtectedMode())
                    .answersRevealTime(request.answersRevealTime())
                    .deadline(request.deadline())
                    .durationMinutes(request.durationMinutes())
                    .creatorId(request.creatorId())
                    .images(images)
                    .createdAt(LocalDateTime.now())
                    .build();
            TestDomain response = testRepository.save(test);

            log.info("Created test with id: {}", response.getId());
            return testMapper.toDto(response);
        } catch (Exception e) {
            removeImages(uploadedImages);
            throw e;
        }
    }

    @Override
    public TestDto updateTest(@Valid TestUpdateRequest request) {
        TestDomain test = getTestById(request.testId());
        Set<String> previousImages = test.getImages().stream()
                .map(Image::getFilename)
                .collect(Collectors.toSet());
        Set<String> uploadedImages = uploadImages(request.images());
        TestDomain response;

        try {
            Set<Image> newImages = uploadedImages.stream()
                    .map(filename -> Image.builder().filename(filename).build())
                    .collect(Collectors.toSet());

            test.setTitle(getOrDefault(request.title(), test.getTitle()));
            test.setDescription(getOrDefault(request.description(), test.getDescription()));
            test.setIsProtectedMode(getOrDefault(request.isProtectedMode(), test.getIsProtectedMode()));
            test.setAnswersRevealTime(getOrDefault(request.answersRevealTime(), test.getAnswersRevealTime()));
            test.setDeadline(getOrDefault(request.deadline(), test.getDeadline()));
            test.setDurationMinutes(getOrDefault(request.durationMinutes(), test.getDurationMinutes()));
            test.setCreatorId(getOrDefault(request.creatorId(), test.getCreatorId()));
            test.setImages(newImages);
            test.setUpdatedAt(LocalDateTime.now());

            response = testRepository.save(test);
        } catch (Exception e) {
            removeImages(uploadedImages);
            throw new TestException(e.getMessage());
        }

        removeImages(previousImages);
        log.info("Updated test with id: {}", response.getId());
        return testMapper.toDto(response);
    }

    private TestDomain getTestById(UUID id) {
        return testRepository.findById(id).orElseThrow(
                () -> new TestException("Test with id " + id + " not found"));
    }

    private Set<String> uploadImages(Set<MultipartFile> images) {
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
            imageServiceApi.removeByFilename(filename, ImageSubfolder.EDUCATION_TEST);
        }
    }
}
