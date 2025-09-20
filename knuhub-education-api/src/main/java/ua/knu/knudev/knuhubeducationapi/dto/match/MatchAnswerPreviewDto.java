package ua.knu.knudev.knuhubeducationapi.dto.match;

import lombok.Builder;
import ua.knu.knudev.knuhubeducationapi.dto.TestAttemptLiteDto;

import java.util.Set;
import java.util.UUID;

@Builder
public record MatchAnswerPreviewDto(
        UUID id,
        TestAttemptLiteDto testAttempt,
        MatchQuestionLiteDto matchQuestion,
        Set<MatchingPairLiteDto> matchingPairs
) {
}
