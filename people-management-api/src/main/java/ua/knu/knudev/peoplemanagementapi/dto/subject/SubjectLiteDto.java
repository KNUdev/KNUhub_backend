package ua.knu.knudev.peoplemanagementapi.dto.subject;

import lombok.Builder;
import ua.knu.knudev.knuhubcommon.dto.MultiLanguageFieldDto;

import java.util.UUID;

@Builder
public record SubjectLiteDto(
        UUID id,
        MultiLanguageFieldDto name
) {
}
