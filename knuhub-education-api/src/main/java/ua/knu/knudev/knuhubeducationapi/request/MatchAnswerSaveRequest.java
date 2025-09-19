package ua.knu.knudev.knuhubeducationapi.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.util.HashMap;
import java.util.UUID;

@Builder
@Schema(description = "Request object for creating or updating a match answer")
public record MatchAnswerSaveRequest(

        @NotNull
        @Schema(
                description = "ID of the match question for which the answer is submitted",
                requiredMode = Schema.RequiredMode.REQUIRED,
                example = "550e8400-e29b-41d4-a716-446655440000"
        )
        UUID questionId,

        @NotNull
        @Schema(
                description = "ID of the student's test attempt to which this answer belongs",
                requiredMode = Schema.RequiredMode.REQUIRED,
                example = "d290f1ee-6c54-4b01-90e6-d701748f0851"
        )
        UUID testAttemptId,

        @NotNull
        @Schema(
                description = "The student's selected matching pairs for the question. " +
                        "Each map should contain exactly one entry where the key is the ID of an item from the left column, " +
                        "and the value is the ID of the corresponding item from the right column.",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        HashMap<UUID, UUID> matchingPairs
) {
}
