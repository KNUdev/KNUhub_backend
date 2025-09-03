package ua.knu.knudev.knuhubeducationapi.dto;

import lombok.Builder;

import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;

@Builder
public record OptionQuestionPreviewDto(
        UUID id,
        String text,
        BigDecimal maxMark,
        Set<OptionPreviewDto> options,
        Set<ImageLiteDto> images
) {
}
