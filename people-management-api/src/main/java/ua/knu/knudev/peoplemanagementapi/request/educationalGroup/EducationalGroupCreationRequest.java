package ua.knu.knudev.peoplemanagementapi.request.educationalGroup;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import ua.knu.knudev.knuhubcommon.dto.MultiLanguageFieldDto;

import java.util.List;
import java.util.UUID;

@Builder
public record EducationalGroupCreationRequest(
        @NotNull(message = "Name cannot be null")
        MultiLanguageFieldDto name,
        List<UUID> studentsIds,
        List<UUID> teachersIds,
        List<UUID> subjectsIds,
        List<String> educationalSpecialtyIds
) {
}
