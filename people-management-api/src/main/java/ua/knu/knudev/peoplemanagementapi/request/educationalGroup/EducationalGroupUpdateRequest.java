package ua.knu.knudev.peoplemanagementapi.request.educationalGroup;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.util.UUID;

@Builder
public record EducationalGroupUpdateRequest(
        @NotNull(message = "Educational group ID cannot be null")
        UUID educationalGroupId,
        String newEnName,
        String newUkName
) {
}
