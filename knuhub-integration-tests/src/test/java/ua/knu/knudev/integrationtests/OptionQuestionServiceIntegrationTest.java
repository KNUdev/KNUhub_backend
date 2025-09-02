package ua.knu.knudev.integrationtests;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.multipart.MultipartFile;
import ua.knu.knudev.fileserviceapi.api.ImageServiceApi;
import ua.knu.knudev.fileserviceapi.subfolder.ImageSubfolder;
import ua.knu.knudev.integrationtests.config.IntegrationTestsConfig;
import ua.knu.knudev.knuhubcommon.constant.AnswersRevealTime;
import ua.knu.knudev.knuhubcommon.constant.OptionQuestionType;
import ua.knu.knudev.knuhubeducation.domain.TestDomain;
import ua.knu.knudev.knuhubeducation.repository.OptionQuestionRepository;
import ua.knu.knudev.knuhubeducation.repository.TestRepository;
import ua.knu.knudev.knuhubeducation.service.OptionQuestionService;
import ua.knu.knudev.knuhubeducationapi.dto.OptionQuestionLiteDto;
import ua.knu.knudev.knuhubeducationapi.exception.OptionQuestionException;
import ua.knu.knudev.knuhubeducationapi.request.OptionCreationRequest;
import ua.knu.knudev.knuhubeducationapi.request.OptionQuestionCreationRequest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest(classes = IntegrationTestsConfig.class)
@ActiveProfiles("test")
public class OptionQuestionServiceIntegrationTest {

    private final Set<String> uploadedImages = new HashSet<>();
    private final MultipartFile image = new MockMultipartFile("image1.jpeg", "image1.jpeg", "image/jpeg", "image1".getBytes());

    @Autowired
    private OptionQuestionService optionQuestionService;
    @Autowired
    private OptionQuestionRepository optionQuestionRepository;
    @Autowired
    private TestRepository testRepository;
    @Autowired
    private ImageServiceApi imageServiceApi;

    private TestDomain test;

    @BeforeEach
    public void setUp() {
        test = createTest();
    }

    @AfterEach
    public void tearDown() {
        optionQuestionRepository.deleteAll();
        uploadedImages.forEach(uploadedAvatarFile -> {
            try {
                imageServiceApi.removeByFilename(uploadedAvatarFile, ImageSubfolder.EDUCATION_TEST);
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        });
    }

    private TestDomain createTest() {
        TestDomain test = TestDomain.builder()
                .title("Test")
                .description("Description")
                .isProtectedMode(false)
                .answersRevealTime(AnswersRevealTime.ON_SUBMIT)
                .deadline(LocalDateTime.of(2030, 1, 1, 1, 1))
                .durationMinutes(60)
                .creatorId(UUID.randomUUID())
                .createdAt(LocalDateTime.now())
                .build();

        return testRepository.save(test);
    }

    @Nested
    @DisplayName("Create optionQuestion scenarios")
    class CreateOptionQuestionScenarios {

        @Test
        @DisplayName("Should successfully create optionQuestion when provided valid request")
        public void should_successfullyCreateOptionQuestion_When_providedValidRequest() {
            OptionQuestionCreationRequest request = OptionQuestionCreationRequest.builder()
                    .testId(test.getId())
                    .text("test")
                    .questionType(OptionQuestionType.ONE_ANSWER)
                    .maxMark(new BigDecimal("2"))
                    .options(getOptionCreationRequests())
                    .images(Set.of(image))
                    .build();

            OptionQuestionLiteDto response = optionQuestionService.create(request);

            assertNotNull(response);
            assertNotNull(response.id());
            assertEquals(test.getId(), response.test().id());
            assertEquals(request.text(), response.text());
            assertEquals(request.maxMark(), response.maxMark());
            assertEquals(request.options().size(), response.options().size());
            assertEquals(request.images().size(), response.imagesPaths().size());
        }

        @Test
        @DisplayName("Should throw exception when creating question without text and images")
        public void should_throwException_when_creatingQuestionWithoutTextAndImages() {
            OptionQuestionCreationRequest request = OptionQuestionCreationRequest.builder()
                    .testId(test.getId())
                    .questionType(OptionQuestionType.ONE_ANSWER)
                    .maxMark(new BigDecimal("2"))
                    .options(getOptionCreationRequests())
                    .build();

            assertThrows(OptionQuestionException.class, () -> optionQuestionService.create(request));
        }

    }

    private Set<OptionCreationRequest> getOptionCreationRequests() {
        Set<OptionCreationRequest> requests = new HashSet<>();

        for (int i = 0; i < 3; i++) {
            OptionCreationRequest wrongAnswerRequest = OptionCreationRequest.builder()
                    .text("Option " + 1)
                    .isCorrect(false)
                    .image(image)
                    .build();
            requests.add(wrongAnswerRequest);
        }
        OptionCreationRequest correctAnswerRequest = OptionCreationRequest.builder()
                .text("Option " + 1)
                .isCorrect(true)
                .image(image)
                .build();
        requests.add(correctAnswerRequest);

        return requests;
    }
}
