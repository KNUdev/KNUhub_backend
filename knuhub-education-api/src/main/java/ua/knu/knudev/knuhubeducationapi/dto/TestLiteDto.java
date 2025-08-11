package ua.knu.knudev.knuhubeducationapi.dto;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record TestLiteDto(
        UUID id,
        String title,
        String description,
        Boolean isProtectedMode,
        Boolean canAnswersBeAccessedOnSubmit,
        LocalDateTime deadline,
        Integer durationMinutes,
        UUID creatorId,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
