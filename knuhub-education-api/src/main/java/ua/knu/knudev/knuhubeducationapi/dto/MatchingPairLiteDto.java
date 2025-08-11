package ua.knu.knudev.knuhubeducationapi.dto;

import lombok.Builder;

import java.util.UUID;

@Builder
public record MatchingPairLiteDto(
        UUID id,
        MatchingLeftLiteDto matchingLeft,
        MatchingRightLiteDto matchingRight
) {
}
