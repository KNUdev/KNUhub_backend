package ua.knu.knudev.knuhubeducationapi.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import org.springframework.web.multipart.MultipartFile;
import ua.knu.knudev.knuhubcommon.constant.OptionQuestionType;

import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;

@Builder
@Schema(description = "Request object for creating an option question (ONE_ANSWER, MULTI_ANSWERS)")
public record OptionQuestionCreationRequest(

        @NotNull
        @Schema(
                description = "Id of the test which will own created question",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        UUID testId,

        @Size(max = 5000)
        @Schema(
                description = "Text attached to question. If text is null, images field must contain at least one image",
                maxLength = 5000
        )
        String text,

        @NotNull
        @Schema(
                description = "Option question type (ONE_ANSWER or MULTI_ANSWER)",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        OptionQuestionType questionType,

        @Schema(
                description = "Max possible mark for correct answer. Number can contain a maximum of 3 digits before the decimal point and 3 after it",
                defaultValue = "1"
        )
        BigDecimal maxMark,

        @NotNull
        @Size(min = 2, max = 20)
        @Schema(
                description = "Options of the question. There must be more than 1 option and less than 21 options",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        Set<OptionCreationRequest> options,

        @NotNull
        @Size(max = 3)
        @Schema(
                description = "Images attached to the question. Maximum 3 images. If images field has 0 length, text field must be not null",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        Set<MultipartFile> images
) {
}
