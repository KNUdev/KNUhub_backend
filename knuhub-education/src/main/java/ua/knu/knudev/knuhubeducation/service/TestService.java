package ua.knu.knudev.knuhubeducation.service;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;
import ua.knu.knudev.fileserviceapi.api.ImageServiceApi;
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

import static ua.knu.knudev.knuhubeducation.service.HelperService.getOrDefault;

@Service
@RequiredArgsConstructor
@Validated
@Slf4j
public class TestService implements TestApi {

    private final TestRepository testRepository;
//    private final ImageServiceApi imageServiceApi;
    private final TestMapper testMapper;

    @Override
    public TestDto createTest(@Valid TestCreationRequest request) {
        TestDomain test = TestDomain.builder()
                .title(request.title())
                .description(request.description())
                .isProtectedMode(request.isProtectedMode())
                .answersRevealTime(request.answersRevealTime())
                .deadline(request.deadline())
                .durationMinutes(request.durationMinutes())
                .creatorId(request.creatorId())
                .createdAt(LocalDateTime.now())
                .build();

        TestDomain response = testRepository.save(test);

        log.info("Created test with id: {}", response.getId());
        return testMapper.toDto(response);
    }

    @Override
    public TestDto updateTest(@Valid TestUpdateRequest request) {
        TestDomain test = getTestById(request.testId());

        test.setTitle(getOrDefault(request.title(), test.getTitle()));
        test.setDescription(getOrDefault(request.description(), test.getDescription()));
        test.setIsProtectedMode(getOrDefault(request.isProtectedMode(), test.getIsProtectedMode()));
        test.setAnswersRevealTime(getOrDefault(request.answersRevealTime(), test.getAnswersRevealTime()));
        test.setDeadline(getOrDefault(request.deadline(), test.getDeadline()));
        test.setDurationMinutes(getOrDefault(request.durationMinutes(), test.getDurationMinutes()));
        test.setCreatorId(getOrDefault(request.creatorId(), test.getCreatorId()));
        test.setUpdatedAt(LocalDateTime.now());

        TestDomain response = testRepository.save(test);

        log.info("Updated test with id: {}", response.getId());
        return testMapper.toDto(response);
    }

    private TestDomain getTestById(UUID id) {
        return testRepository.findById(id).orElseThrow(
                () -> new TestException("Test with id " + id + " not found"));
    }

//    private Set<String> uploadImages(Set<MultipartFile> images) {
//        Set<String> imageFilenames = new HashSet<>();
//
//        for (MultipartFile image : images) {
//            if (ObjectUtils.isEmpty(image)) {
//                return null;
//            }
//
//            String filename = imageServiceApi.uploadFile(problemPhoto, imageName, subfolder);
//            imageFilenames.add(filename);
//        }
//
//        return imageFilenames;
//    }
}
