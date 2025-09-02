package ua.knu.knudev.peoplemanagementapi.dto.teacher;

import lombok.Builder;
import ua.knu.knudev.knuhubcommon.dto.FullNameDto;
import ua.knu.knudev.peoplemanagementapi.dto.DeaneryWorkerDto;
import ua.knu.knudev.peoplemanagementapi.dto.educationalGroup.EducationalGroupLiteDto;
import ua.knu.knudev.peoplemanagementapi.dto.educationalSpecialty.EducationalSpecialtyLiteDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Builder
public record TeacherDto(
        UUID id,
        String knuEmail,
        String commonEmail,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        FullNameDto fullName,
        String phoneNumber,
        String scientificMotto,
        List<EducationalSpecialtyLiteDto> specialties,
        List<EducationalGroupLiteDto> educationalGroups,
        DeaneryWorkerDto deaneryWorker
) {
}
