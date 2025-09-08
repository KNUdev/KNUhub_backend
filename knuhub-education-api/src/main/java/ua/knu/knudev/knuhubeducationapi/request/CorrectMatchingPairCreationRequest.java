package ua.knu.knudev.knuhubeducationapi.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
@Schema(description = "Request object for creating a matching pair which is one of the correct answers to the question")
public record CorrectMatchingPairCreationRequest(

        @NotBlank
        @Size(max = 1000)
        @Schema(
                description = "Text on the left side, which is the correct matching to 'matchingRight'. Can not be blank",
                maxLength = 1000,
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        String matchingLeft,

        @NotBlank
        @Size(max = 1000)
        @Schema(
                description = "Text on the right side, which is the correct matching to 'matchingLeft'. Can not be blank",
                maxLength = 1000,
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        String matchingRight
) {
}
