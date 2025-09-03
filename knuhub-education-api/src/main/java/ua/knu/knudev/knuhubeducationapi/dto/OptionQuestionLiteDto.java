package ua.knu.knudev.knuhubeducationapi.dto;

import lombok.Builder;
import ua.knu.knudev.knuhubcommon.constant.OptionQuestionType;

import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;

@Builder
public record OptionQuestionLiteDto(
        UUID id,
        String text,
        OptionQuestionType type,
        BigDecimal maxMark,
        TestPreviewDto test,
        Set<OptionLiteDto> options,
        Set<ImageLiteDto> images
) {
}
