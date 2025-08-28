package ua.knu.knudev.knuhubeducationapi.dto;

import lombok.Builder;

import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;

@Builder
public record OptionQuestionLiteDto(
        UUID id,
        String text,
        BigDecimal maxMark,
        Set<OptionLiteDto> options,
        Set<String> imagesPaths
) {
}
