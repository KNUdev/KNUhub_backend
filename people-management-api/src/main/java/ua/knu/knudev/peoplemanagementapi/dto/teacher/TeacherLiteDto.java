package ua.knu.knudev.peoplemanagementapi.dto.teacher;

import lombok.Builder;
import ua.knu.knudev.knuhubcommon.dto.FullNameDto;
import ua.knu.knudev.peoplemanagementapi.dto.DeaneryWorkerDto;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record TeacherLiteDto(
        UUID id,
        String knuEmail,
        String commonEmail,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        FullNameDto fullName,
        String phoneNumber,
        String scientificMotto,
        DeaneryWorkerDto deaneryWorker
) {
}
