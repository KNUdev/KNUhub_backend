package ua.knu.knudev.knuhubeducationapi.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import org.springframework.web.multipart.MultipartFile;

@Builder
@Schema(description = "Request object for creating an option")
public record OptionCreationRequest(

        @Size(max = 5000)
        @Schema(
                description = "Text attached to option. If text field is null, image must not be null"
        )
        String text,

        @NotNull
        @Schema(
                description = "Is this option a correct answer",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        Boolean isCorrect,

        @Schema(
                description = "Image attached to option. If image field is null, text field must not be null"
        )
        MultipartFile image
) {
}
