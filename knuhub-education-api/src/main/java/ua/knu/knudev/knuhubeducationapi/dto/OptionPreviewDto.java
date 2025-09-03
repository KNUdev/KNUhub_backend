package ua.knu.knudev.knuhubeducationapi.dto;

import lombok.Builder;

import java.util.UUID;

@Builder
public record OptionPreviewDto(
        UUID id,
        String text,
        ImageLiteDto image
) {
}
