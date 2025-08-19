package ua.knu.knudev.peoplemanagementapi.request.educationalGroup;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.util.List;
import java.util.UUID;

@Builder
public record EducationalGroupReceivingRequest(
        @Schema(description = "Substring which can be part of educational group name, faculty name," +
                " student name, teacher name, subject name or educational specialty name")
        String searchQuery,

        @Schema(description = "List of educational specialty codes")
        List<String> educationalSpecialtyCodeNames,

        @Schema(description = "List of student IDs")
        List<UUID> studentIds,

        @Schema(description = "List of teacher IDs")
        List<UUID> teacherIds,

        @Schema(description = "List of subject IDs")
        List<UUID> subjectIds,

        @Schema(description = "Page number")
        Integer pageNumber,

        @Schema(description = "Number of employees per page")
        Integer pageSize
) {
}
