package ua.knu.knudev.peoplemanagementapi.dto.user;

import lombok.Builder;
import ua.knu.knudev.knuhubcommon.dto.FullNameDto;

import java.time.LocalDateTime;
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
