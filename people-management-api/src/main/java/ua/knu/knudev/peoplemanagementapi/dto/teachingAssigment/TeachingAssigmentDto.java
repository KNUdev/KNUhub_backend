package ua.knu.knudev.peoplemanagementapi.dto.teachingAssigment;

import lombok.Builder;
import ua.knu.knudev.knuhubcommon.constant.Semester;
import ua.knu.knudev.knuhubcommon.constant.StudyCourse;
import ua.knu.knudev.peoplemanagementapi.dto.educationalGroup.EducationalGroupLiteDto;
import ua.knu.knudev.peoplemanagementapi.dto.educationalSpecialty.EducationalSpecialtyDto;
import ua.knu.knudev.peoplemanagementapi.dto.teacher.TeacherLiteDto;

import java.util.List;
import java.util.UUID;

@Builder
public record TeachingAssigmentDto(
        UUID id,
        StudyCourse studyCourse,
        Semester semester,
        List<TeacherLiteDto> teachers,
        List<EducationalGroupLiteDto> groups,
        List<EducationalSpecialtyDto> specialties
) {
}
