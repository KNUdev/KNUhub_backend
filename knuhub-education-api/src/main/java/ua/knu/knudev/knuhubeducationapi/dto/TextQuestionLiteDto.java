package ua.knu.knudev.knuhubeducationapi.dto;

import lombok.Builder;

import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;

@Builder
public record TextQuestionLiteDto(
        UUID id,
        String text,
        BigDecimal maxMark,
        Set<String> correctAnswers,
        Boolean isCaseSensitive,
        Set<ImageLiteDto> images
) {
}
