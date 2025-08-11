package ua.knu.knudev.knuhubeducationapi.dto;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Builder
public record FullTestDto(
        UUID id,
        String title,
        String description,
        Boolean isProtectedMode,
        Boolean canAnswersBeAccessedOnSubmit,
        LocalDateTime deadline,
        Integer durationMinutes,
        UUID creatorId,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        Set<OptionQuestionLiteDto> optionQuestions,
        Set<TextQuestionLiteDto> textQuestions,
        Set<MatchQuestionLiteDto> matchQuestions,
        Set<ImageLiteDto> images,
        Set<TestAttemptLiteDto> attempts
) {
}
