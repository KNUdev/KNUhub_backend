package ua.knu.knudev.knuhubsecurityapi.response;

import lombok.Builder;
import ua.knu.knudev.knuhubcommon.constant.UserRole;

import java.util.UUID;

@Builder
public record EmployeeRegistrationResponse(
        UUID id,
        String email,
        UserRole role
) {
}
