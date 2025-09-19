package ua.knu.knudev.knuhubeducation.service;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ua.knu.knudev.knuhubeducation.domain.*;
import ua.knu.knudev.knuhubeducation.domain.matching.MatchAnswer;
import ua.knu.knudev.knuhubeducation.domain.matching.MatchQuestion;
import ua.knu.knudev.knuhubeducation.domain.matching.MatchingPair;
import ua.knu.knudev.knuhubeducation.mapper.MatchAnswerPreviewMapper;
import ua.knu.knudev.knuhubeducation.mapper.OptionAnswerPreviewMapper;
import ua.knu.knudev.knuhubeducation.mapper.TestAttemptMapper;
import ua.knu.knudev.knuhubeducation.mapper.TextAnswerPreviewMapper;
import ua.knu.knudev.knuhubeducation.repository.*;
import ua.knu.knudev.knuhubeducationapi.api.TestAttemptApi;
import ua.knu.knudev.knuhubeducationapi.dto.OptionAnswerPreviewDto;
import ua.knu.knudev.knuhubeducationapi.dto.TestAttemptDto;
import ua.knu.knudev.knuhubeducationapi.dto.TextAnswerPreviewDto;
import ua.knu.knudev.knuhubeducationapi.dto.match.MatchAnswerPreviewDto;
import ua.knu.knudev.knuhubeducationapi.exception.*;
import ua.knu.knudev.knuhubeducationapi.request.MatchAnswerSaveRequest;
import ua.knu.knudev.knuhubeducationapi.request.OptionAnswerSaveRequest;
import ua.knu.knudev.knuhubeducationapi.request.TestAttemptCreationRequest;
import ua.knu.knudev.knuhubeducationapi.request.TextAnswerSaveRequest;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Validated
@Slf4j
public class TestAttemptService implements TestAttemptApi {

    private final TestAttemptRepository testAttemptRepository;
    private final TestRepository testRepository;
    private final OptionQuestionRepository optionQuestionRepository;
    private final TextQuestionRepository textQuestionRepository;
    private final MatchQuestionRepository matchQuestionRepository;
    private final TestAttemptMapper testAttemptMapper;
    private final OptionAnswerPreviewMapper optionAnswerPreviewMapper;
    private final TextAnswerPreviewMapper textAnswerPreviewMapper;
    private final MatchAnswerPreviewMapper matchAnswerPreviewMapper;

    @Override
    public TestAttemptDto createTestAttempt(@Valid TestAttemptCreationRequest request) {
        TestDomain test = testRepository.findById(request.testId()).orElseThrow(
                () -> new TestException("Test with id " + request.testId() + " not found"));

        TestAttempt attempt = TestAttempt.builder()
                .test(test)
                .startTime(LocalDateTime.now())
                .build();

        TestAttempt response = testAttemptRepository.save(attempt);
        return testAttemptMapper.toDto(response);
    }

    @Override
    public void deleteTestAttempt(UUID id) {
        testAttemptRepository.deleteById(id);
        log.info("Deleted test attempt with id {}", id);
    }

    @Override
    @Transactional
    public OptionAnswerPreviewDto saveOptionAnswer(OptionAnswerSaveRequest request) {
        TestAttempt attempt = getTestAttemptById(request.testAttemptId());
        OptionQuestion question = optionQuestionRepository.findById(request.questionId())
                .orElseThrow(() -> new OptionQuestionException("Question with id " + request.questionId() + " not found"));

        checkIsDeadlinePassed(attempt);

        Set<Option> existingOptions = question.getOptions();
        Set<UUID> existingOptionsIds = existingOptions.stream()
                .map(Option::getId)
                .collect(Collectors.toSet());

        if (!existingOptionsIds.containsAll(request.chosenOptionsIds())) {
            throw new OptionQuestionException("Some chosen options do not exist or do not belong to the question " + request.questionId());
        }

        OptionAnswer answer = OptionAnswer.builder()
                .question(question)
                .chosenOptions(
                        existingOptions.stream()
                                .filter(o -> request.chosenOptionsIds().contains(o.getId()))
                                .collect(Collectors.toSet())
                )
                .build();
        attempt.addOptionAnswer(answer);

        testAttemptRepository.save(attempt);
        return optionAnswerPreviewMapper.toDto(answer);
    }

    @Override
    @Transactional
    public TextAnswerPreviewDto saveTextAnswer(TextAnswerSaveRequest request) {
        TestAttempt attempt = getTestAttemptById(request.testAttemptId());
        TextQuestion question = textQuestionRepository.findById(request.questionId())
                .orElseThrow(() -> new TextQuestionException("Question with id " + request.questionId() + " not found"));

        checkIsDeadlinePassed(attempt);

        TextAnswer answer = TextAnswer.builder()
                .answer(request.answer())
                .question(question)
                .build();

        attempt.addTextAnswer(answer);

        testAttemptRepository.save(attempt);
        return textAnswerPreviewMapper.toDto(answer);
    }

    @Override
    @Transactional
    public MatchAnswerPreviewDto saveMatchAnswer(MatchAnswerSaveRequest request) {
        TestAttempt attempt = getTestAttemptById(request.testAttemptId());
        MatchQuestion question = matchQuestionRepository.findById(request.questionId())
                .orElseThrow(() -> new MatchQuestionException("Question with id " + request.questionId() + " not found"));

        checkIsDeadlinePassed(attempt);

        MatchAnswer answer = MatchAnswer.builder()
                .matchingPairs(createMatchingPairs(request.matchingPairs(), question.getCorrectMatchingPairs()))
                .matchQuestion(question)
                .build();
        attempt.addMatchAnswer(answer);

        testAttemptRepository.save(attempt);
        return matchAnswerPreviewMapper.toDto(answer);
    }

    private Set<MatchingPair> createMatchingPairs(HashMap<UUID, UUID> answerMatchingPairs, Set<MatchingPair> correctPairs) {
        Set<UUID> existingLeftColumn = correctPairs.stream()
                .map(pair -> pair.getMatchingLeft().getId())
                .collect(Collectors.toSet());
        Set<UUID> existingRightColumn = correctPairs.stream()
                .map(pair -> pair.getMatchingRight().getId())
                .collect(Collectors.toSet());
        Set<UUID> answerLeftColumn = answerMatchingPairs.keySet();
        Set<UUID> answerRightColumn = (Set<UUID>) answerMatchingPairs.values();

        if (answerRightColumn.size() != answerLeftColumn.size()) {
            throw new TestAttemptException("Can not create matching answer. Values in right column must not repeat");
        }
        if (!existingLeftColumn.containsAll(answerLeftColumn) || !existingRightColumn.containsAll(answerRightColumn)) {
            throw new TestAttemptException("Can not create matching answer. " +
                    "There is not existing element in left or right column");
        }

        return createMatchingPairs(answerMatchingPairs.entrySet(), correctPairs);
    }

    private Set<MatchingPair> createMatchingPairs(Set<Map.Entry<UUID, UUID>> matchingPairsEntries, Set<MatchingPair> correctPairs) {
        Set<MatchingPair> newMatchingPairs = new HashSet<>();

        for (Map.Entry<UUID, UUID> entry : matchingPairsEntries) {
            UUID leftId = entry.getKey();
            UUID rightId = entry.getValue();

            MatchingPair pair = MatchingPair.builder()
                    .matchingLeft(correctPairs.stream()
                            .filter(p -> p.getMatchingLeft().getId().equals(leftId))
                            .findFirst()
                            .orElseThrow(() -> new TestAttemptException("Can not create matching answer. " +
                                    "There is not existing element in left column"))
                            .getMatchingLeft())
                    .matchingRight(correctPairs.stream()
                            .filter(p -> p.getMatchingRight().getId().equals(rightId))
                            .findFirst()
                            .orElseThrow(() -> new TestAttemptException("Can not create matching answer. " +
                                    "There is not existing element in right column"))
                            .getMatchingRight())
                    .build();

            newMatchingPairs.add(pair);
        }
        return newMatchingPairs;
    }

    private void checkIsDeadlinePassed(TestAttempt attempt) {
        if (attempt.getTest().getDeadline().isBefore(LocalDateTime.now())) {
            throw new TestAttemptException("Test is already closed. Can not change answers " +
                    "of the test attempt with id " + attempt.getId());
        }
    }

    private TestAttempt getTestAttemptById(UUID id) {
        return testAttemptRepository.findById(id).orElseThrow(
                () -> new TestAttemptException("Test attempt with id " + id + " not found"));
    }
}
