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
import ua.knu.knudev.knuhubeducation.domain.TestDomain;
import ua.knu.knudev.knuhubeducation.domain.TextQuestion;
import ua.knu.knudev.knuhubeducation.repository.TestRepository;
import ua.knu.knudev.knuhubeducation.repository.TextQuestionRepository;
import ua.knu.knudev.knuhubeducation.service.TextQuestionService;
import ua.knu.knudev.knuhubeducationapi.dto.ImageLiteDto;
import ua.knu.knudev.knuhubeducationapi.dto.TextQuestionLiteDto;
import ua.knu.knudev.knuhubeducationapi.exception.TextQuestionException;
import ua.knu.knudev.knuhubeducationapi.request.TextQuestionCreationRequest;
import ua.knu.knudev.knuhubeducationapi.request.TextQuestionUpdateRequest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest(classes = IntegrationTestsConfig.class)
@ActiveProfiles("test")
public class TextQuestionServiceIntegrationTest {

    private final Set<String> uploadedImages = new HashSet<>();
    private final MultipartFile image = new MockMultipartFile("image1.jpeg", "image1.jpeg", "image/jpeg", "image1".getBytes());

    @Autowired
    private TextQuestionService textQuestionService;
    @Autowired
    private TextQuestionRepository textQuestionRepository;
    @Autowired
    private TestRepository testRepository;
    @Autowired
    private ImageServiceApi imageServiceApi;

    private TestDomain test;
    private TextQuestion textQuestion;

    @BeforeEach
    public void setUp() {
        test = createTest();
        textQuestion = createTextQuestion(test);
    }

    @AfterEach
    public void tearDown() {
        textQuestionRepository.deleteAll();
        testRepository.deleteAll();
        uploadedImages.forEach(uploadedFile -> {
            try {
                imageServiceApi.removeByFilename(uploadedFile, ImageSubfolder.EDUCATION_TEST);
            } catch (Exception ignored) {
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

    private TextQuestion createTextQuestion(TestDomain test) {
        TextQuestion textQuestion = new TextQuestion();
        textQuestion.setTest(test);
        textQuestion.setText("text");
        textQuestion.setMaxMark(new BigDecimal("50"));
        textQuestion.setIsCaseSensitive(true);
        textQuestion.setCorrectAnswers(Set.of("Hello", "World"));

        return textQuestionRepository.save(textQuestion);
    }

    Set<String> getImageFilenames(Set<ImageLiteDto> images) {
        return images.stream()
                .map(ImageLiteDto::filename)
                .collect(Collectors.toSet());
    }

    @Nested
    @DisplayName("Create textQuestion scenarios")
    class CreateTextQuestionScenarios {

        @Test
        @DisplayName("Should successfully create textQuestion when provided valid data")
        void should_successfullyCreateTextQuestion_when_providedValidData() {
            TextQuestionCreationRequest request = TextQuestionCreationRequest.builder()
                    .text("text")
                    .testId(test.getId())
                    .maxMark(new BigDecimal("10"))
                    .isCaseSensitive(true)
                    .correctAnswers(Set.of("first", "second"))
                    .images(Set.of(image))
                    .build();

            TextQuestionLiteDto response = textQuestionService.create(request);
            uploadedImages.addAll(getImageFilenames(response.images()));

            assertNotNull(response);
            assertEquals(request.text(), response.text());
            assertEquals(request.maxMark(), response.maxMark());
            assertEquals(request.isCaseSensitive(), response.isCaseSensitive());
            assertEquals(request.correctAnswers().size(), response.correctAnswers().size());
            assertEquals(request.images().size(), response.images().size());
        }

        @Test
        @DisplayName("Should throw exception when creating question without text and images")
        void should_throwException_when_creatingQuestionWithoutTextAndImages() {
            TextQuestionCreationRequest request = TextQuestionCreationRequest.builder()
                    .testId(test.getId())
                    .build();

            assertThrows(TextQuestionException.class, () -> textQuestionService.create(request));
        }
    }

    @Nested
    @DisplayName("Update textQuestion scenarios")
    class UpdateTextQuestionScenarios {

        @Test
        @DisplayName("Should successfully update question when provided valid data")
        void should_successfullyUpdateTextQuestion_when_providedValidData() {
            TextQuestionUpdateRequest request = TextQuestionUpdateRequest.builder()
                    .questionId(textQuestion.getId())
                    .testId(test.getId())
                    .text("new text")
                    .maxMark(new BigDecimal("123.1"))
                    .correctAnswers(Set.of("first"))
                    .isCaseSensitive(false)
                    .images(Set.of(image))
                    .build();

            TextQuestionLiteDto response = textQuestionService.update(request);
            uploadedImages.addAll(getImageFilenames(response.images()));

            assertNotNull(response);
            assertEquals(request.text(), response.text());
            assertEquals(request.maxMark(), response.maxMark());
            assertEquals(request.isCaseSensitive(), response.isCaseSensitive());
            assertEquals(request.correctAnswers().size(), response.correctAnswers().size());
            assertEquals(request.images().size(), response.images().size());
        }

        @Test
        @DisplayName("Should throw exception when updating not existing textQuestion")
        void should_throwException_when_updatingNotExistingTextQuestion() {
            TextQuestionUpdateRequest request = TextQuestionUpdateRequest.builder()
                    .questionId(UUID.randomUUID())
                    .text("new text")
                    .build();

            assertThrows(TextQuestionException.class, () -> textQuestionService.update(request));
        }
    }
}
