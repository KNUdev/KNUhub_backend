package ua.knu.knudev.peoplemanagementapi.request.educationalSpecialty;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.util.List;
import java.util.UUID;

@Builder
public record EducationalSpecialtyChangeRelationsRequest(
        @NotNull(message = "Code name cannot be null")
        String codeName,
        List<UUID> facultyIds,
        List<UUID> educationalGroupIds,
        List<UUID> studentIds,
        List<UUID> teacherIds,
        List<UUID> teachingAssignmentIds
) {
}
