package ua.knu.knudev.peoplemanagementapi.request;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.util.UUID;

@Builder
public record FacultyUpdateRequest(
        @NotNull(message = "facultyId can`t be null")
        UUID facultyId,
        String newFacultyEnName,
        String newFacultyUkName
) {
}
