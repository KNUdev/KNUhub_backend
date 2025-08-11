package ua.knu.knudev.knuhubeducationapi.dto;

import lombok.Builder;

import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;

@Builder
public record MatchQuestionLiteDto(
        UUID id,
        String text,
        BigDecimal maxMark,
        Set<MatchingPairLiteDto> correctMatchingPairs,
        Set<ImageLiteDto> images
) {
}
