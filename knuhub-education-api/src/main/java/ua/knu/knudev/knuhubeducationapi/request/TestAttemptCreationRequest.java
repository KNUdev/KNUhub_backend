package ua.knu.knudev.knuhubeducationapi.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.util.UUID;

@Builder
@Schema(description = "Request object for creating a test attempt")
public record TestAttemptCreationRequest(

        @NotNull
        @Schema(
                description = "Id of a student who takes the test",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        UUID studentId,

        @NotNull
        @Schema(
                description = "ID of the test that the student is attempting",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        UUID testId

) {
}
