package ua.knu.knudev.peoplemanagementapi.dto;

import lombok.Builder;
import ua.knu.knudev.knuhubcommon.dto.MultiLanguageFieldDto;

import java.util.Set;
import java.util.UUID;

@Builder
public record EducationalGroupDto(
        UUID id,
        MultiLanguageFieldDto name,
        Set<StudentDto> students,
        Set<TeacherDto> teachers,
        Set<SubjectDto> subjects,
        Set<EducationalSpecialtyDto> educationalSpecialties
) {
}
