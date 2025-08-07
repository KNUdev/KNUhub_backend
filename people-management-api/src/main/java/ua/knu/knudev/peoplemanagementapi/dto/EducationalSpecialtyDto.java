package ua.knu.knudev.peoplemanagementapi.dto;

import lombok.Builder;
import ua.knu.knudev.knuhubcommon.dto.MultiLanguageFieldDto;

import java.util.List;

@Builder
public record EducationalSpecialtyDto(
        String codeName,
        MultiLanguageFieldDto name,
        List<FacultyDto> faculties,
        List<EducationalGroupDto> groups,
        List<StudentDto> students,
        List<TeacherDto> teachers,
        List<TeachingAssigmentDto> teachingAssigments
) {
}
