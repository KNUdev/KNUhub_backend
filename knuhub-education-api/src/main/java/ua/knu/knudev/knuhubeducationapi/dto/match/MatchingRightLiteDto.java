package ua.knu.knudev.knuhubeducationapi.dto.match;

import lombok.Builder;

import java.util.UUID;

@Builder
public record MatchingRightLiteDto(
        UUID id,
        String text
) {
}
