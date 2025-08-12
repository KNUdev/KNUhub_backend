package ua.knu.knudev.peoplemanagementapi.dto;

import lombok.Builder;
import ua.knu.knudev.knuhubcommon.constant.StudyCourse;
import ua.knu.knudev.knuhubcommon.dto.FullNameDto;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record StudentLiteDto(
        UUID id,
        String knuEmail,
        String commonEmail,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        FullNameDto fullName,
        String phoneNumber,
        Boolean isHeadman,
        String studentCardNumber,
        StudyCourse studyCourse
) {
}
