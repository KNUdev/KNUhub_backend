package ua.knu.knudev.peoplemanagementapi.dto;

import lombok.Builder;
import ua.knu.knudev.knuhubcommon.dto.MultiLanguageFieldDto;

import java.util.List;

@Builder
public record FacultyDto(
        MultiLanguageFieldDto facultyName,
        List<EducationalSpecialtyDto> educationalSpecialtyDtos,
        List<EducationalGroupDto> educationalGroupDtos,
        List<UserDto> userDtos
) {
}
