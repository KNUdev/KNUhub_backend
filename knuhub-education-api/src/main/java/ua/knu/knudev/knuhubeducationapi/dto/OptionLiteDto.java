package ua.knu.knudev.knuhubeducationapi.dto;

import lombok.Builder;

import java.util.UUID;

@Builder
public record OptionLiteDto(
        UUID id,
        String text,
        Boolean isCorrect,
        String imagePath
) {
}
