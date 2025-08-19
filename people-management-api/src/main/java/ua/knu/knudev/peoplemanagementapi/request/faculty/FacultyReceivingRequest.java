package ua.knu.knudev.peoplemanagementapi.request.faculty;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.util.List;
import java.util.UUID;

@Builder
public record FacultyReceivingRequest(
        @Schema(description = "Substring which can be part of faculty name, educational specialty name," +
                " or educational group name or user name")
        String searchQuery,

        @Schema(description = "List of educational specialty codes")
        List<String> educationalSpecialtyCodeNames,

        @Schema(description = "List of educational group IDs")
        List<UUID> educationalGroupIds,

        @Schema(description = "List of user`s IDs")
        List<UUID> userIds,

        @Schema(description = "Page number")
        Integer pageNumber,

        @Schema(description = "Number of employees per page")
        Integer pageSize
) {
}
