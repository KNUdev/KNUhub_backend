package ua.knu.knudev.knuhubeducationapi.dto;

import lombok.Builder;
import ua.knu.knudev.knuhubcommon.constant.AnswersRevealTime;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Builder
public record TestPreviewDto(
        UUID id,
        String title,
        String description,
        Boolean isProtectedMode,
        AnswersRevealTime answersRevealTime,
        LocalDateTime deadline,
        Integer durationMinutes,
        UUID creatorId,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        Set<ImageLiteDto> images
) {
}
