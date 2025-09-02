package ua.knu.knudev.peoplemanagementapi.dto.educationalSpecialty;

import lombok.Builder;
import ua.knu.knudev.knuhubcommon.dto.MultiLanguageFieldDto;
import ua.knu.knudev.peoplemanagementapi.dto.faculty.FacultyLiteDto;
import ua.knu.knudev.peoplemanagementapi.dto.student.StudentLiteDto;
import ua.knu.knudev.peoplemanagementapi.dto.teacher.TeacherLiteDto;
import ua.knu.knudev.peoplemanagementapi.dto.teachingAssigment.TeachingAssigmentLiteDto;
import ua.knu.knudev.peoplemanagementapi.dto.educationalGroup.EducationalGroupLiteDto;

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
