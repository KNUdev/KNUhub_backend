package ua.knu.knudev.peoplemanagementapi.dto.subject;

import lombok.Builder;
import ua.knu.knudev.knuhubcommon.dto.MultiLanguageFieldDto;
import ua.knu.knudev.peoplemanagementapi.dto.educationalGroup.EducationalGroupLiteDto;

import java.util.List;
import java.util.UUID;

@Builder
public record SubjectDto(
        UUID id,
        MultiLanguageFieldDto name,
        List<EducationalGroupLiteDto> educationalGroups
) {
}
