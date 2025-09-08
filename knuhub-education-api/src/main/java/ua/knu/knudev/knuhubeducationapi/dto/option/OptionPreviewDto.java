package ua.knu.knudev.knuhubeducationapi.dto.option;

import lombok.Builder;
import ua.knu.knudev.knuhubeducationapi.dto.ImageLiteDto;

import java.util.UUID;

@Builder
public record OptionPreviewDto(
        UUID id,
        String text,
        ImageLiteDto image
) {
}
