package ua.knu.knudev.peoplemanagementapi.request.educationalSpecialty;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.util.List;
import java.util.UUID;

@Builder
public record EducationalSpecialtyReceivingRequest(

        @Schema(description = "Substring which can be part of educational specialty name, faculty name," +
                " or user name")
        String searchQuery,

        @Schema(description = "List of teaching assignment IDs")
        List<UUID> teachingAssigmentIds,

        @Schema(description = "List of user IDs")
        List<UUID> educationalGroupIds,

        @Schema(description = "List of student`s IDs")
        List<UUID> studentIds,

        @Schema(description = "List of teacher`s IDs")
        List<UUID> teacherIds,

        @Schema(description = "Page number")
        Integer pageNumber,

        @Schema(description = "Number of employees per page")
        Integer pageSize
) {
}
