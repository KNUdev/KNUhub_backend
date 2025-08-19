package ua.knu.knudev.peoplemanagementapi.dto.user;

import lombok.Builder;
import ua.knu.knudev.knuhubcommon.dto.FullNameDto;
import ua.knu.knudev.peoplemanagementapi.dto.faculty.FacultyLiteDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Builder
public record UserDto(
        UUID id,
        String knuEmail,
        String commonEmail,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        FullNameDto fullName,
        String phoneNumber,
        List<FacultyLiteDto> faculties
) {
}
