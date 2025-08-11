package ua.knu.knudev.peoplemanagementapi.request;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import ua.knu.knudev.knuhubcommon.dto.MultiLanguageFieldDto;

import java.util.List;
import java.util.UUID;

@Builder
public record EducationalSpecialtyCreationRequest(
        @NotNull(message = "Code name cannot be null")
        String codeName,

        @NotNull(message = "Name cannot be null")
        MultiLanguageFieldDto name,
        List<UUID> facultyIds,
        List<UUID> educationalGroupIds,
        List<UUID> studentIds,
        List<UUID> teacherIds,
        List<UUID> teachingAssigmentIds
) {
}
