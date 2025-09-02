//package ua.knu.knudev.integrationtests;
//
//import jakarta.validation.ValidationException;
//import lombok.extern.slf4j.Slf4j;
//import org.junit.jupiter.api.*;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.mock.web.MockMultipartFile;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.web.multipart.MultipartFile;
//import ua.knu.knudev.fileserviceapi.api.ImageServiceApi;
//import ua.knu.knudev.fileserviceapi.subfolder.ImageSubfolder;
//import ua.knu.knudev.integrationtests.config.IntegrationTestsConfig;
//import ua.knu.knudev.knuhubcommon.constant.AnswersRevealTime;
//import ua.knu.knudev.knuhubeducation.domain.TestDomain;
//import ua.knu.knudev.knuhubeducation.repository.TestRepository;
//import ua.knu.knudev.knuhubeducation.service.TestService;
//import ua.knu.knudev.knuhubeducationapi.dto.ImageLiteDto;
//import ua.knu.knudev.knuhubeducationapi.dto.TestDto;
//import ua.knu.knudev.knuhubeducationapi.exception.TestException;
//import ua.knu.knudev.knuhubeducationapi.request.TestCreationRequest;
//import ua.knu.knudev.knuhubeducationapi.request.TestUpdateRequest;
//
//import java.time.LocalDateTime;
//import java.util.HashSet;
//import java.util.Set;
//import java.util.UUID;
//import java.util.stream.Collectors;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@Slf4j
//@SpringBootTest(classes = IntegrationTestsConfig.class)
//@ActiveProfiles("test")
//public class TestServiceIntegrationTest {
//
//    private final Set<String> uploadedImages = new HashSet<>();
//
//    @Autowired
//    private TestService testService;
//    @Autowired
//    private TestRepository testRepository;
//    @Autowired
//    private ImageServiceApi imageServiceApi;
//
//    private TestDomain test;
//
//    @BeforeEach
//    public void setup() {
//        test = createNewTest();
//    }
//
//    @AfterEach
//    public void teardown() {
//        testRepository.deleteAll();
//        uploadedImages.forEach(uploadedAvatarFile -> {
//            try {
//                imageServiceApi.removeByFilename(uploadedAvatarFile, ImageSubfolder.EDUCATION_TEST);
//            } catch (Exception e) {
//                log.error(e.getMessage());
//            }
//        });
//    }
//
//    private TestDomain createNewTest() {
//        TestDomain test = TestDomain.builder()
//                .title("Test")
//                .description("Description")
//                .isProtectedMode(false)
//                .answersRevealTime(AnswersRevealTime.ON_SUBMIT)
//                .deadline(LocalDateTime.of(2030, 1, 1, 1, 1))
//                .durationMinutes(60)
//                .creatorId(UUID.randomUUID())
//                .createdAt(LocalDateTime.now())
//                .build();
//
//        return testRepository.save(test);
//    }
//
//    @Nested
//    @DisplayName("Create test scenarios")
//    class CreateNewTestScenarios {
//
//        @Test
//        @DisplayName("Should successfully create new test when provided only necessary fields")
//        public void should_successfullyCreateNewTest_When_providedNecessaryFields() {
//            TestCreationRequest request = TestCreationRequest.builder()
//                    .title("Test")
//                    .description("Description")
//                    .isProtectedMode(false)
//                    .answersRevealTime(AnswersRevealTime.ON_SUBMIT)
//                    .durationMinutes(60)
//                    .creatorId(UUID.randomUUID())
//                    .build();
//
//            TestDto response = testService.createTest(request);
//
//            assertNotNull(response);
//            assertNotNull(response.id());
//            assertEquals(request.title(), response.title());
//            assertEquals(request.description(), response.description());
//            assertEquals(request.isProtectedMode(), response.isProtectedMode());
//            assertEquals(request.answersRevealTime(), response.answersRevealTime());
//            assertEquals(request.durationMinutes(), response.durationMinutes());
//            assertEquals(request.creatorId(), response.creatorId());
//        }
//
//        @Test
//        @DisplayName("Should successfully create new test when provided all fields")
//        public void should_successfullyCreateNewTest_When_providedAllFields() {
//            Set<MultipartFile> imageFiles = new HashSet<>();
//            imageFiles.add(new MockMultipartFile("image1.jpeg", "image1.jpeg", "image/jpeg", "image1".getBytes()));
//            imageFiles.add(new MockMultipartFile("image2.jpeg", "image2.jpeg", "image/jpeg", "image2".getBytes()));
//            imageFiles.add(new MockMultipartFile("image3.jpeg", "image3.jpeg", "image/jpeg", "image3".getBytes()));
//
//            TestCreationRequest request = TestCreationRequest.builder()
//                    .title("Test")
//                    .description("Description")
//                    .isProtectedMode(false)
//                    .answersRevealTime(AnswersRevealTime.ON_SUBMIT)
//                    .durationMinutes(60)
//                    .creatorId(UUID.randomUUID())
//                    .images(imageFiles)
//                    .build();
//
//            TestDto response = testService.createTest(request);
//            uploadedImages.addAll(response.images().stream()
//                    .map(ImageLiteDto::filename).collect(Collectors.toSet()));
//
//            assertNotNull(response);
//            assertNotNull(response.id());
//            assertEquals(request.title(), response.title());
//            assertEquals(request.description(), response.description());
//            assertEquals(request.isProtectedMode(), response.isProtectedMode());
//            assertEquals(request.answersRevealTime(), response.answersRevealTime());
//            assertEquals(request.durationMinutes(), response.durationMinutes());
//            assertEquals(request.creatorId(), response.creatorId());
//            assertEquals(3, response.images().size());
//        }
//
//        @Test
//        @DisplayName("Should throw exception when provided more than 3 images ")
//        public void Should_throwException_When_providedMoreThanThreeImages() {
//            Set<MultipartFile> imageFiles = new HashSet<>();
//            imageFiles.add(new MockMultipartFile("image1.jpeg", "image1.jpeg", "image/jpeg", "image1".getBytes()));
//            imageFiles.add(new MockMultipartFile("image2.jpeg", "image2.jpeg", "image/jpeg", "image2".getBytes()));
//            imageFiles.add(new MockMultipartFile("image3.jpeg", "image3.jpeg", "image/jpeg", "image3".getBytes()));
//            imageFiles.add(new MockMultipartFile("image4.jpeg", "image4.jpeg", "image/jpeg", "image4".getBytes()));
//
//            TestCreationRequest request = TestCreationRequest.builder()
//                    .title("Test")
//                    .description("Description")
//                    .isProtectedMode(false)
//                    .answersRevealTime(AnswersRevealTime.ON_SUBMIT)
//                    .durationMinutes(60)
//                    .creatorId(UUID.randomUUID())
//                    .images(imageFiles)
//                    .build();
//
//            assertThrows(ValidationException.class, () -> testService.createTest(request));
//        }
//    }
//
//    @Nested
//    @DisplayName("Update test scenarios")
//    class UpdateTestScenarios {
//
//        @Test
//        @DisplayName("Should successfully update test when provided all fields")
//        public void should_successfullyUpdateTest_When_providedAllFields() {
//            Set<MultipartFile> imageFiles = new HashSet<>();
//            imageFiles.add(new MockMultipartFile("image1.jpeg", "image1.jpeg", "image/jpeg", "image1".getBytes()));
//            imageFiles.add(new MockMultipartFile("image2.jpeg", "image2.jpeg", "image/jpeg", "image2".getBytes()));
//            imageFiles.add(new MockMultipartFile("image3.jpeg", "image3.jpeg", "image/jpeg", "image3".getBytes()));
//            UUID creatorId = UUID.randomUUID();
//
//            TestUpdateRequest request = TestUpdateRequest.builder()
//                    .testId(test.getId())
//                    .title("NewTitle")
//                    .description("NewDescription")
//                    .isProtectedMode(true)
//                    .answersRevealTime(AnswersRevealTime.AFTER_DEADLINE)
//                    .deadline(LocalDateTime.of(2040, 1, 1, 0, 0))
//                    .durationMinutes(20)
//                    .creatorId(creatorId)
//                    .images(imageFiles)
//                    .build();
//
//            TestDto response = testService.updateTest(request);
//            uploadedImages.addAll(response.images().stream()
//                    .map(ImageLiteDto::filename).collect(Collectors.toSet()));
//
//            assertNotNull(response);
//            assertEquals(request.title(), response.title());
//            assertEquals(request.description(), response.description());
//            assertEquals(request.isProtectedMode(), response.isProtectedMode());
//            assertEquals(request.answersRevealTime(), response.answersRevealTime());
//            assertEquals(request.durationMinutes(), response.durationMinutes());
//            assertEquals(request.creatorId(), response.creatorId());
//            assertEquals(3, response.images().size());
//        }
//
//        @Test
//        @DisplayName("Should throw exception when trying to update not existing test")
//        public void should_throwException_When_tryingToUpdateNotExistingTest() {
//            TestUpdateRequest request = TestUpdateRequest.builder()
//                    .testId(UUID.randomUUID())
//                    .title("NewTitle")
//                    .build();
//
//            assertThrows(TestException.class, () -> testService.updateTest(request));
//        }
//    }
//}
