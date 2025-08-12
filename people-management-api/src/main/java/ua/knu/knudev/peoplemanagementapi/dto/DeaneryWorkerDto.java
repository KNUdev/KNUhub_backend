package ua.knu.knudev.peoplemanagementapi.dto;

import lombok.Builder;
import ua.knu.knudev.knuhubcommon.dto.FullNameDto;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record DeaneryWorkerDto(
        UUID id,
        String knuEmail,
        String commonEmail,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        FullNameDto fullName,
        String phoneNumber,
        WorkerProfessionDto profession,
        TeacherDto teacher
) {
}
