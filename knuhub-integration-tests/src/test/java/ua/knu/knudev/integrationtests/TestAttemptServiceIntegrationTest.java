package ua.knu.knudev.integrationtests;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import ua.knu.knudev.fileserviceapi.api.ImageServiceApi;
import ua.knu.knudev.integrationtests.config.IntegrationTestsConfig;
import ua.knu.knudev.knuhubcommon.constant.AnswersRevealTime;
import ua.knu.knudev.knuhubcommon.constant.OptionQuestionType;
import ua.knu.knudev.knuhubeducation.domain.*;
import ua.knu.knudev.knuhubeducation.domain.matching.*;
import ua.knu.knudev.knuhubeducation.repository.*;
import ua.knu.knudev.knuhubeducation.service.TestAttemptService;
import ua.knu.knudev.knuhubeducationapi.dto.OptionAnswerPreviewDto;
import ua.knu.knudev.knuhubeducationapi.dto.TestAttemptDto;
import ua.knu.knudev.knuhubeducationapi.dto.TextAnswerPreviewDto;
import ua.knu.knudev.knuhubeducationapi.dto.match.MatchAnswerPreviewDto;
import ua.knu.knudev.knuhubeducationapi.exception.TestException;
import ua.knu.knudev.knuhubeducationapi.request.MatchAnswerSaveRequest;
import ua.knu.knudev.knuhubeducationapi.request.OptionAnswerSaveRequest;
import ua.knu.knudev.knuhubeducationapi.request.TestAttemptCreationRequest;
import ua.knu.knudev.knuhubeducationapi.request.TextAnswerSaveRequest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest(classes = IntegrationTestsConfig.class)
@ActiveProfiles("test")
public class TestAttemptServiceIntegrationTest {

    @Autowired
    private TestAttemptService attemptService;
    @Autowired
    private TestAttemptRepository attemptRepository;
    @Autowired
    private TestRepository testRepository;
    @Autowired
    private OptionQuestionRepository optionQuestionRepository;
    @Autowired
    private TextQuestionRepository textQuestionRepository;
    @Autowired
    private MatchQuestionRepository matchQuestionRepository;

    private TestDomain test;
    private TestAttempt testAttempt;
    private OptionQuestion optionQuestion;
    private TextQuestion textQuestion;
    private MatchQuestion matchQuestion;
    @Autowired
    private TestAttemptService testAttemptService;

    @BeforeEach
    public void setUp() {
        test = createNewTest();
        optionQuestion = createOptionQuestion();
        textQuestion = createTextQuestion();
        matchQuestion = createMatchQuestion();
        testAttempt = createNewAttempt(test);
        createOptionAnswer();
        createTextAnswer();
        createMatchAnswer();
    }

    @AfterEach
    public void tearDown() {
        attemptRepository.deleteAll();
        testRepository.deleteAll();
    }

    private TestDomain createNewTest() {
        TestDomain test = TestDomain.builder()
                .title("Test")
                .description("Description")
                .isProtectedMode(false)
                .answersRevealTime(AnswersRevealTime.ON_SUBMIT)
                .deadline(LocalDateTime.of(2030, 1, 1, 1, 1))
                .durationMinutes(987_654_321)
                .creatorId(UUID.randomUUID())
                .createdAt(LocalDateTime.now())
                .build();

        return testRepository.save(test);
    }

    private TestAttempt createNewAttempt(TestDomain test) {
        TestAttempt attempt = TestAttempt.builder()
                .startTime(LocalDateTime.of(2025, 1, 1, 1, 1))
                .studentId(UUID.randomUUID())
                .test(test)
                .build();

        return attemptRepository.save(attempt);
    }

    private OptionQuestion createOptionQuestion() {
        Set<Option> options = new HashSet<>();
        for (int i = 0; i < 10; i++) {
            Option option = Option.builder()
                    .text("option " + i)
                    .isCorrect(true)
                    .build();

            options.add(option);
        }

        OptionQuestion optionQuestion = OptionQuestion.builder()
                .text("question text")
                .maxMark(new BigDecimal("50"))
                .type(OptionQuestionType.ONE_ANSWER)
                .options(new HashSet<>())
                .test(test)
                .build();
        optionQuestion.addOptions(options);

        return optionQuestionRepository.save(optionQuestion);
    }

    private TextQuestion createTextQuestion() {
        TextQuestion textQuestion = new TextQuestion();
        textQuestion.setTest(test);
        textQuestion.setText("text");
        textQuestion.setMaxMark(new BigDecimal("50"));
        textQuestion.setIsCaseSensitive(true);
        textQuestion.setCorrectAnswers(Set.of("Hello", "World"));

        return textQuestionRepository.save(textQuestion);
    }

    private MatchQuestion createMatchQuestion() {
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

    private OptionAnswer createOptionAnswer() {
        Set<Option> options = optionQuestion.getOptions();
        Option chosenOption = options.iterator().next();

        OptionAnswer answer = OptionAnswer.builder()
                .question(optionQuestion)
                .chosenOptions(Set.of(chosenOption))
                .build();
        testAttempt.addOptionAnswer(answer);

        attemptRepository.save(testAttempt);
        return answer;
    }

    private TextAnswer createTextAnswer() {
        TextAnswer answer = TextAnswer.builder()
                .testAttempt(testAttempt)
                .question(textQuestion)
                .answer("myAnswer")
                .build();
        testAttempt.addTextAnswer(answer);

        attemptRepository.save(testAttempt);
        return answer;
    }

    private MatchAnswer createMatchAnswer() {
        MatchingPair correctPair = matchQuestion.getCorrectMatchingPairs().iterator().next();
        MatchingPair answerPair = MatchingPair.builder()
                .matchingLeft(correctPair.getMatchingLeft())
                .matchingRight(correctPair.getMatchingRight())
                .build();

        MatchAnswer answer = MatchAnswer.builder()
                .matchQuestion(matchQuestion)
                .matchingPairs(Set.of(answerPair))
                .build();
        testAttempt.addMatchAnswer(answer);

        attemptRepository.save(testAttempt);
        return answer;
    }

    @Nested
    @DisplayName("Test attempt creation scenarios")
    class TestAttemptCreationScenarios {

        @Test
        @DisplayName("Should successfully create attempt when provided valid data")
        void should_successfullyCreateAttempt_when_providedValidData() {
            TestAttemptCreationRequest request = TestAttemptCreationRequest.builder()
                    .studentId(UUID.randomUUID())
                    .testId(test.getId())
                    .build();

            TestAttemptDto response = testAttemptService.createTestAttempt(request);

            assertNotNull(response);
            assertNotNull(response.id());
            assertNotNull(response.startTime());
            assertEquals(request.testId(), response.test().id());
            assertEquals(request.studentId(), response.studentId());
        }

        @Test
        @DisplayName("Should throw exception when creating attempt with not existing test")
        void should_throwException_when_CreatingAttemptWithNotExistingTest() {
            TestAttemptCreationRequest request = TestAttemptCreationRequest.builder()
                    .studentId(UUID.randomUUID())
                    .testId(UUID.randomUUID())
                    .build();

            assertThrows(TestException.class, () -> testAttemptService.createTestAttempt(request));
        }
    }

    @Test
    @DisplayName("Should successfully save option answer when provided valid data")
    void should_successfullySaveOptionAnswer_when_providedValidData() {
        UUID chosenOptionId = optionQuestion.getOptions().iterator().next().getId();

        OptionAnswerSaveRequest request = OptionAnswerSaveRequest.builder()
                .questionId(optionQuestion.getId())
                .testAttemptId(testAttempt.getId())
                .chosenOptionsIds(Set.of(chosenOptionId))
                .build();

        OptionAnswerPreviewDto response = attemptService.saveOptionAnswer(request);

        assertNotNull(response);
        assertNotNull(response.id());
        assertEquals(request.chosenOptionsIds().iterator().next(),
                response.chosenOptions().iterator().next().id());
        assertEquals(request.testAttemptId(), response.testAttempt().id());
        assertEquals(request.questionId(), response.question().id());
    }

    @Test
    @DisplayName("Should successfully save text answer when provided valid data")
    @Transactional
    void should_successfullySaveTextAnswer_when_providedValidData() {
        TextAnswerSaveRequest request = TextAnswerSaveRequest.builder()
                .questionId(textQuestion.getId())
                .testAttemptId(testAttempt.getId())
                .answer("myAnswer")
                .build();

        TextAnswerPreviewDto response = attemptService.saveTextAnswer(request);

        assertNotNull(response);
        assertNotNull(response.id());
        assertEquals(request.answer(), response.answer());
        assertEquals(request.testAttemptId(), response.testAttempt().id());
        assertEquals(request.questionId(), response.question().id());
        assertEquals(1, testAttemptService.findById(testAttempt.getId()).textAnswers().size());
    }

    @Test
    @DisplayName("Should successfully save match answer when provided valid data")
    @Transactional
    void should_successfullySaveMatchAnswer_when_providedValidData() {
        HashMap<UUID, UUID> matchingPairsIds = new HashMap<>();
        MatchingPair pair = matchQuestion.getCorrectMatchingPairs().iterator().next();
        matchingPairsIds.put(pair.getMatchingLeft().getId(), pair.getMatchingRight().getId());

        MatchAnswerSaveRequest request = MatchAnswerSaveRequest.builder()
                .questionId(matchQuestion.getId())
                .testAttemptId(testAttempt.getId())
                .matchingPairs(matchingPairsIds)
                .build();

        MatchAnswerPreviewDto response = attemptService.saveMatchAnswer(request);

        assertNotNull(response);
        assertNotNull(response.id());
        assertEquals(request.testAttemptId(), response.testAttempt().id());
        assertEquals(request.questionId(), response.matchQuestion().id());
        assertEquals(request.matchingPairs().size(), response.matchingPairs().size());
        assertEquals(1, testAttemptService.findById(testAttempt.getId()).textAnswers().size());
    }
}
