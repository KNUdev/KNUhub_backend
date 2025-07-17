package ua.knu.knudev.knuhubsecurityapi.dto;

import lombok.Builder;
import ua.knu.knudev.knuhubcommon.constant.UserRole;

import java.util.UUID;

@Builder
public record AuthenticatedEmployeeDto(
        UUID id,
        String email,
        String password,
        UserRole role,
        boolean enabled,
        boolean nonLocked
) {
}
