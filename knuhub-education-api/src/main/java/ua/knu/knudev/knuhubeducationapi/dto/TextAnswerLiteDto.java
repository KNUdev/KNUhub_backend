package ua.knu.knudev.knuhubeducationapi.dto;

import lombok.Builder;

import java.math.BigDecimal;
import java.util.UUID;

@Builder
public record TextAnswerLiteDto(
        UUID id,
        BigDecimal mark,
        String answer,
        Boolean isMarkedAsCorrect,
        TextQuestionLiteDto question
) {
}
