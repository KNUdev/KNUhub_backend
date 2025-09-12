package ua.knu.knudev.knuhubeducationapi.dto.match;

import lombok.Builder;
import ua.knu.knudev.knuhubeducationapi.dto.ImageLiteDto;
import ua.knu.knudev.knuhubeducationapi.dto.test.TestPreviewDto;

import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;

@Builder
public record MatchQuestionLiteDto(
        UUID id,
        String text,
        BigDecimal maxMark,
        Set<MatchingPairLiteDto> correctMatchingPairs,
        Set<ImageLiteDto> images,
        TestPreviewDto test
) {
}
