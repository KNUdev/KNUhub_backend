package ua.knu.knudev.knuhubeducationapi.dto.option;

import lombok.Builder;
import ua.knu.knudev.knuhubeducationapi.dto.ImageLiteDto;

import java.util.UUID;

@Builder
public record OptionLiteDto(
        UUID id,
        String text,
        Boolean isCorrect,
        ImageLiteDto image
) {
}
