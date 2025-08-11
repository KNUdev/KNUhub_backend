package ua.knu.knudev.knuhubeducationapi.dto;

import lombok.Builder;

import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;

@Builder
public record MatchAnswerLiteDto(
        UUID id,
        BigDecimal mark,
        MatchQuestionLiteDto matchQuestion,
        Set<MatchingPairLiteDto> matchingPairs
) {
}
