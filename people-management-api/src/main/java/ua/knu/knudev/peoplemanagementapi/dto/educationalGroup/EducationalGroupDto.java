package ua.knu.knudev.peoplemanagementapi.dto.educationalGroup;

import lombok.Builder;
import ua.knu.knudev.knuhubcommon.dto.MultiLanguageFieldDto;
import ua.knu.knudev.peoplemanagementapi.dto.educationalSpecialty.EducationalSpecialtyDto;
import ua.knu.knudev.peoplemanagementapi.dto.educationalSpecialty.EducationalSpecialtyLiteDto;
import ua.knu.knudev.peoplemanagementapi.dto.student.StudentLiteDto;
import ua.knu.knudev.peoplemanagementapi.dto.subject.SubjectLiteDto;
import ua.knu.knudev.peoplemanagementapi.dto.teacher.TeacherLiteDto;

import java.util.Set;
import java.util.UUID;

@Builder
public record EducationalGroupDto(
        UUID id,
        MultiLanguageFieldDto name,
        Set<StudentLiteDto> students,
        Set<TeacherLiteDto> teachers,
        Set<SubjectLiteDto> subjects,
        Set<EducationalSpecialtyLiteDto> educationalSpecialties
) {
}
