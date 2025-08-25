package ua.knu.knudev.knuhubeducationapi.request;

import lombok.Builder;
import ua.knu.knudev.knuhubeducationapi.dto.ImageLiteDto;
import ua.knu.knudev.knuhubeducationapi.dto.OptionLiteDto;

import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;

@Builder
public record OptionQuestionCreationRequest(
        UUID testId,
        String text,
        BigDecimal maxMark,
        Set<OptionLiteDto> options,
        Set<ImageLiteDto> images
) {
}
