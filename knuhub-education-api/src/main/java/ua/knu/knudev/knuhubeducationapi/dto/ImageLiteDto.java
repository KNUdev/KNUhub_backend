package ua.knu.knudev.knuhubeducationapi.dto;

import lombok.Builder;

import java.util.UUID;

@Builder
public record ImageLiteDto(
        UUID id,
        String filename
) {
}
