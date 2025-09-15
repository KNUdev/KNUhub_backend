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
import ua.knu.knudev.knuhubeducation.domain.matching.MatchQuestion;
import ua.knu.knudev.knuhubeducation.domain.matching.MatchingLeft;
import ua.knu.knudev.knuhubeducation.domain.matching.MatchingPair;
import ua.knu.knudev.knuhubeducation.domain.matching.MatchingRight;
import ua.knu.knudev.knuhubeducation.repository.MatchQuestionRepository;
import ua.knu.knudev.knuhubeducation.repository.TestRepository;
import ua.knu.knudev.knuhubeducation.service.MatchQuestionService;
import ua.knu.knudev.knuhubeducationapi.dto.ImageLiteDto;
import ua.knu.knudev.knuhubeducationapi.dto.match.MatchQuestionLiteDto;
import ua.knu.knudev.knuhubeducationapi.exception.MatchQuestionException;
import ua.knu.knudev.knuhubeducationapi.request.CorrectMatchingPairCreationRequest;
import ua.knu.knudev.knuhubeducationapi.request.MatchQuestionCreationRequest;
import ua.knu.knudev.knuhubeducationapi.request.MatchQuestionUpdateRequest;

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
public class MatchQuestionServiceIntegrationTest {

    private final Set<String> uploadedImages = new HashSet<>();
    private final MultipartFile image = new MockMultipartFile("image1.jpeg", "image1.jpeg", "image/jpeg", "image1".getBytes());

    @Autowired
    private MatchQuestionService matchQuestionService;
    @Autowired
    private MatchQuestionRepository matchQuestionRepository;
    @Autowired
    private TestRepository testRepository;
    @Autowired
    private ImageServiceApi imageServiceApi;

    private TestDomain test;
    private MatchQuestion matchQuestion;

    @BeforeEach
    public void setUp() {
        test = createTest();
        matchQuestion = createMatchQuestion(test);
    }

    @AfterEach
    public void tearDown() {
        matchQuestionRepository.deleteAll();
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

    private MatchQuestion createMatchQuestion(TestDomain test) {
        Set<MatchingPair> matchingPairs = getMatchingPairs();

        MatchQuestion matchQuestion = MatchQuestion.builder()
                .text("match question")
                .maxMark(new BigDecimal("50"))
                .correctMatchingPairs(matchingPairs)
                .test(test)
                .build();

        return matchQuestionRepository.save(matchQuestion);
    }

    private Set<MatchingPair> getMatchingPairs() {
        Set<MatchingPair> matchingPairs = new HashSet<>();
        for (int i = 0; i < 5; i++) {
            MatchingLeft left = MatchingLeft.builder()
                    .text("left" + i)
                    .build();
            MatchingRight right = MatchingRight.builder()
                    .text("right" + i)
                    .build();
            MatchingPair pair = MatchingPair.builder()
                    .matchingLeft(left)
                    .matchingRight(right)
                    .build();

            matchingPairs.add(pair);
        }

        return matchingPairs;
    }

    private Set<CorrectMatchingPairCreationRequest> getMatchingPairsRequests() {
        Set<CorrectMatchingPairCreationRequest> requests = new HashSet<>();
        Set<MatchingPair> matchingPairs = getMatchingPairs();
        for (MatchingPair pair : matchingPairs) {
            CorrectMatchingPairCreationRequest request = CorrectMatchingPairCreationRequest.builder()
                    .matchingLeft(pair.getMatchingLeft().getText())
                    .matchingRight(pair.getMatchingRight().getText())
                    .build();

            requests.add(request);
        }

        return requests;
    }

    Set<String> getImageFilenames(Set<ImageLiteDto> images) {
        return images.stream()
                .map(ImageLiteDto::filename)
                .collect(Collectors.toSet());
    }

    @Nested
    @DisplayName("Create matchQuestion scenarios")
    class CreateMatchQuestionScenarios {

        @Test
        @DisplayName("Should successfully create matchQuestion when provided valid data")
        public void should_successfullyCreateMatchQuestion_when_providedValidData() {
            MatchQuestionCreationRequest request = MatchQuestionCreationRequest.builder()
                    .testId(test.getId())
                    .text("text")
                    .maxMark(new BigDecimal("2"))
                    .matchingPairs(getMatchingPairsRequests())
                    .images(Set.of(image))
                    .build();

            MatchQuestionLiteDto response = matchQuestionService.create(request);
            uploadedImages.addAll(getImageFilenames(response.images()));

            assertNotNull(response);
            assertNotNull(response.id());
            assertEquals(test.getId(), response.test().id());
            assertEquals(request.text(), response.text());
            assertEquals(request.maxMark(), response.maxMark());
            assertEquals(request.matchingPairs().size(), response.correctMatchingPairs().size());
            assertEquals(request.images().size(), response.images().size());
        }

        @Test
        @DisplayName("Should throw exception when creating question without text and images")
        public void should_throwException_when_creatingQuestionWithoutTextAndImages() {
            MatchQuestionCreationRequest request = MatchQuestionCreationRequest.builder()
                    .testId(test.getId())
                    .matchingPairs(getMatchingPairsRequests())
                    .build();

            assertThrows(MatchQuestionException.class, () -> matchQuestionService.create(request));
        }
    }

    @Nested
    @DisplayName("Update matchQuestion scenarios")
    class UpdateMatchQuestionScenarios {

        @Test
        @DisplayName("Should successfully update question when provided valid data")
        public void should_successfullyUpdateMatchQuestion_when_providedValidData() {
            Set<CorrectMatchingPairCreationRequest> newMatchingPairRequests = getMatchingPairsRequests();
            newMatchingPairRequests.addAll(getMatchingPairsRequests());

            MatchQuestionUpdateRequest request = MatchQuestionUpdateRequest.builder()
                    .questionId(matchQuestion.getId())
                    .testId(test.getId())
                    .text("new text")
                    .maxMark(new BigDecimal("13"))
                    .matchingPairs(newMatchingPairRequests)
                    .images(Set.of(image))
                    .build();

            MatchQuestionLiteDto response = matchQuestionService.update(request);
            uploadedImages.addAll(getImageFilenames(response.images()));

            assertNotNull(response);
            assertNotNull(response.id());
            assertEquals(test.getId(), response.test().id());
            assertEquals(request.text(), response.text());
            assertEquals(request.maxMark(), response.maxMark());
            assertEquals(request.matchingPairs().size(), response.correctMatchingPairs().size());
            assertEquals(request.images().size(), response.images().size());
        }
    }

    @Test
    @DisplayName("Should throw exception when updating not existing matchQuestion")
    public void should_throwException_when_updatingNotExistingMatchQuestion() {
        Set<CorrectMatchingPairCreationRequest> newMatchingPairRequests = getMatchingPairsRequests();
        newMatchingPairRequests.addAll(getMatchingPairsRequests());

        MatchQuestionUpdateRequest request = MatchQuestionUpdateRequest.builder()
                .questionId(UUID.randomUUID())
                .text("new text")
                .build();

        assertThrows(MatchQuestionException.class, () -> matchQuestionService.update(request));
    }
}
