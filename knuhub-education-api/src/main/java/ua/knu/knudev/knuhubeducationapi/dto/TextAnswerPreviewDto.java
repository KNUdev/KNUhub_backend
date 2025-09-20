package ua.knu.knudev.knuhubeducationapi.dto;

import lombok.Builder;

import java.util.UUID;

@Builder
public record TextAnswerPreviewDto(
        UUID id,
        String answer,
        Boolean isMarkedAsCorrect,
        TestAttemptLiteDto testAttempt,
        TextQuestionLiteDto question
) {
}
