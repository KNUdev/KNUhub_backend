package ua.knu.knudev.peoplemanagementapi.dto.faculty;

import lombok.Builder;
import ua.knu.knudev.knuhubcommon.dto.MultiLanguageFieldDto;

import java.util.UUID;

@Builder
public record FacultyLiteDto(
        UUID id,
        MultiLanguageFieldDto name
) {
}
