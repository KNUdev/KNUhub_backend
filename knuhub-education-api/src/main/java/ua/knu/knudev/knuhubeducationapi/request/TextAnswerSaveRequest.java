package ua.knu.knudev.knuhubeducationapi.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.util.UUID;

@Builder
@Schema(description = "Request object for creating or updating a text answer")
public record TextAnswerSaveRequest(

        @NotNull
        @Schema(
                description = "ID of the text question for which the answer is submitted",
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
        @Size(max = 1000)
        @Schema(
                description = "The text provided by the student as the answer to the question",
                maxLength = 1000,
                requiredMode = Schema.RequiredMode.REQUIRED,
                example = "The capital of France is Paris."
        )
        String answer
) {
}
