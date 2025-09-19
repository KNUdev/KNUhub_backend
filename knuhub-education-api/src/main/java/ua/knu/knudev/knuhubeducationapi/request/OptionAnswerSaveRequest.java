package ua.knu.knudev.knuhubeducationapi.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.util.Set;
import java.util.UUID;

@Builder
@Schema(description = "Request object for creating or updating an option answer")
public record OptionAnswerSaveRequest(

        @NotNull
        @Schema(
                description = "ID of the option question for which the answer is submitted",
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
                description = "IDs of the chosen options for the question. " +
                        "Can contain multiple IDs if question is MULTI_ANSWER.",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        Set<UUID> chosenOptionsIds
) {
}
