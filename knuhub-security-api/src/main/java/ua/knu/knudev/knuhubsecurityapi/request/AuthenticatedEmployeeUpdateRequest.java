package ua.knu.knudev.knuhubsecurityapi.request;

import lombok.Builder;
import ua.knu.knudev.knuhubcommon.constant.UserRole;

import java.util.UUID;

@Builder
public record AuthenticatedEmployeeUpdateRequest(
       UUID employeeId,
       String email,
       String oldPassword,
       String newPassword,
       UserRole role,
       boolean isAdminUsage
) {
}
