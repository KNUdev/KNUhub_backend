package ua.knu.knudev.knuhubeducationapi.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;

@Builder
@Schema(description = "Request object for creating a text question")
public record TextQuestionCreationRequest(

        @NotNull
        @Schema(
                description = "Id of the test which will own created question",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        UUID testId,

        @Size(max = 5000)
        @Schema(
                description = "Text attached to question. If text is null, 'images' field must contain at least one image",
                maxLength = 5000
        )
        String text,

        @Schema(
                description = "Max possible mark for correct answer. Number can contain a maximum of 3 digits before the decimal point and 3 after it",
                defaultValue = "1"
        )
        BigDecimal maxMark,

        @Size(max = 50)
        @Schema(
                description = "Correct answers to the question. It will be used to " +
                        "compare with student answers to automatically calculate mark." +
                        "Each string max length is 1000",
                maxLength = 50
        )
        Set<@Size(max = 1000) String> correctAnswers,

        @Schema(
                description = "Indicates whether the comparison of correct answers to student answers should be case-sensitive",
                defaultValue = "false"
        )
        Boolean isCaseSensitive,

        @Size(max = 3)
        @Schema(
                description = "Images attached to the question. Maximum 3 images. If images field has 0 length or is null, 'text' field must be not null"
        )
        Set<MultipartFile> images
) {
}
