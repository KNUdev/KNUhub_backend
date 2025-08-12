package ua.knu.knudev.knuhubeducationapi.dto;

import lombok.Builder;

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
        TestLiteDto test,
        Set<OptionAnswerLiteDto> optionAnswers,
        Set<TextAnswerLiteDto> textAnswers,
        Set<MatchAnswerLiteDto> matchAnswers
) {
}
