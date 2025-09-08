package ua.knu.knudev.knuhubeducationapi.dto;

import lombok.Builder;
import ua.knu.knudev.knuhubeducationapi.dto.option.OptionLiteDto;

import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;

@Builder
public record OptionAnswerLiteDto(
        UUID id,
        BigDecimal mark,
        OptionQuestionLiteDto question,
        Set<OptionLiteDto> chosenOptions

) {
}
