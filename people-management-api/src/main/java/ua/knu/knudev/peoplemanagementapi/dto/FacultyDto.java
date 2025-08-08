package ua.knu.knudev.peoplemanagementapi.dto;

import lombok.Builder;
import ua.knu.knudev.knuhubcommon.dto.MultiLanguageFieldDto;

import java.util.List;
import java.util.UUID;

@Builder
public record FacultyDto(
        UUID id,
        MultiLanguageFieldDto facultyName,
        List<EducationalSpecialtyLiteDto> educationalSpecialties,
        List<UserLiteDto> users
) {
}
