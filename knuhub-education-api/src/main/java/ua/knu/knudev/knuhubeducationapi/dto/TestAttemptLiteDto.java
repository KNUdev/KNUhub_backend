package ua.knu.knudev.knuhubeducationapi.dto;

import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record TestAttemptLiteDto(
        UUID id,
        LocalDateTime startTime,
        LocalDateTime submitTime,
        BigDecimal mark,
        UUID studentId
) {
}
