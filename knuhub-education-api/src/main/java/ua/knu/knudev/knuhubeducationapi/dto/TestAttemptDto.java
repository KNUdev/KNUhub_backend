package ua.knu.knudev.knuhubeducationapi.dto;

import lombok.Builder;
import ua.knu.knudev.knuhubeducationapi.dto.match.MatchAnswerLiteDto;
import ua.knu.knudev.knuhubeducationapi.dto.test.TestPreviewDto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Builder
public record TestAttemptDto(
        UUID id,
        LocalDateTime startTime,
        LocalDateTime submitTime,
        BigDecimal mark,
        UUID studentId,
        TestPreviewDto test,
        Set<OptionAnswerLiteDto> optionAnswers,
        Set<TextAnswerLiteDto> textAnswers,
        Set<MatchAnswerLiteDto> matchAnswers
) {
}
