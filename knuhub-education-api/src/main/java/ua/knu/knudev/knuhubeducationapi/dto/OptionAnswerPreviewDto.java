package ua.knu.knudev.knuhubeducationapi.dto;

import lombok.Builder;
import ua.knu.knudev.knuhubeducationapi.dto.option.OptionLiteDto;

import java.util.Set;
import java.util.UUID;

@Builder
public record OptionAnswerPreviewDto(
        UUID id,
        TestAttemptLiteDto testAttempt,
        OptionQuestionLiteDto question,
        Set<OptionLiteDto> chosenOptions
) {
}
