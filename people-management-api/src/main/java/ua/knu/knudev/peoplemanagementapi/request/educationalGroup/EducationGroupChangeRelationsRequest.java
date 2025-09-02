package ua.knu.knudev.peoplemanagementapi.request.educationalGroup;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.util.List;
import java.util.UUID;

@Builder
public record EducationGroupChangeRelationsRequest(
        @NotNull(message = "Educational Group ID cannot be null")
        UUID educationalGroupId,
        List<UUID> studentIds,
        List<UUID> teacherIds,
        List<UUID> subjectIds,
        List<String> educationalSpecialtyIds
) {
}
