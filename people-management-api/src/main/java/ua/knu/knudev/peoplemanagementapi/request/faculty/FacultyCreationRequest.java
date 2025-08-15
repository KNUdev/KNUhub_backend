package ua.knu.knudev.peoplemanagementapi.request.faculty;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import ua.knu.knudev.knuhubcommon.dto.MultiLanguageFieldDto;

import java.util.List;
import java.util.UUID;

@Builder
public record FacultyCreationRequest(
        @NotNull(message = "facultyName can`t be null")
        MultiLanguageFieldDto facultyName,
        List<String> educationalSpecialtyIds,
        List<UUID> userIds
) {
}
