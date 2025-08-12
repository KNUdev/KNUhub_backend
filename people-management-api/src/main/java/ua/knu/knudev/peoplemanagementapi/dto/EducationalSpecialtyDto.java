package ua.knu.knudev.peoplemanagementapi.dto;

import lombok.Builder;
import ua.knu.knudev.knuhubcommon.dto.MultiLanguageFieldDto;

import java.util.List;

@Builder
public record EducationalSpecialtyDto(
        String codeName,
        MultiLanguageFieldDto name,
        List<FacultyLiteDto> faculties,
        List<EducationalGroupLiteDto> groups,
        List<StudentLiteDto> students,
        List<TeacherLiteDto> teachers,
        List<TeachingAssigmentLiteDto> teachingAssigments
) {
}
