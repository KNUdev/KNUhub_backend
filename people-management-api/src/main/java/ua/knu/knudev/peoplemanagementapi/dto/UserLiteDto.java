package ua.knu.knudev.peoplemanagementapi.dto;

import lombok.Builder;
import ua.knu.knudev.knuhubcommon.dto.FullNameDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Builder
public record UserLiteDto(
        UUID id,
        String knuEmail,
        String commonEmail,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        FullNameDto fullName,
        String phoneNumber
) {
}
