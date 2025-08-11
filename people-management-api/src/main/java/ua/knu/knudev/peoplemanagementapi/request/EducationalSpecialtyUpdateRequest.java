package ua.knu.knudev.peoplemanagementapi.request;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record EducationalSpecialtyUpdateRequest(
        @NotNull(message = "Code name cannot be null")
        String codeName,
        String enSpecialtyName,
        String ukSpecialtyName
) {
}
