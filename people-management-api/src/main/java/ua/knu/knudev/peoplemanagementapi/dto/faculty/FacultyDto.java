package ua.knu.knudev.peoplemanagementapi.dto.faculty;

import lombok.Builder;
import ua.knu.knudev.knuhubcommon.dto.MultiLanguageFieldDto;
import ua.knu.knudev.peoplemanagementapi.dto.user.UserLiteDto;
import ua.knu.knudev.peoplemanagementapi.dto.educationalSpecialty.EducationalSpecialtyLiteDto;

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
