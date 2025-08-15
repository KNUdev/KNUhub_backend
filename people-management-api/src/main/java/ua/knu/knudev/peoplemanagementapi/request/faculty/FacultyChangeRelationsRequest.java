package ua.knu.knudev.peoplemanagementapi.request.faculty;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.util.List;
import java.util.UUID;

@Builder
public record FacultyChangeRelationsRequest(
        @NotNull(message = "Faculty ID cannot be null")
        UUID facultyId,
        List<String> educationalSpecialtyIds,
        List<UUID> userIds
) {
}
