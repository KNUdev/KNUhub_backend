package ua.knu.knudev.peoplemanagementapi.dto.student;

import lombok.Builder;
import ua.knu.knudev.knuhubcommon.constant.StudyCourse;
import ua.knu.knudev.knuhubcommon.dto.FullNameDto;
import ua.knu.knudev.peoplemanagementapi.dto.educationalGroup.EducationalGroupLiteDto;
import ua.knu.knudev.peoplemanagementapi.dto.educationalSpecialty.EducationalSpecialtyDto;
import ua.knu.knudev.peoplemanagementapi.dto.faculty.FacultyLiteDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Builder
public record StudentDto(
        UUID id,
        String knuEmail,
        String commonEmail,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        FullNameDto fullName,
        String phoneNumber,
        Boolean isHeadman,
        String studentCardNumber,
        StudyCourse studyCourse,
        List<FacultyLiteDto> faculties,
        List<EducationalSpecialtyDto> specialties,
        List<EducationalGroupLiteDto> educationalGroups
) {
}
