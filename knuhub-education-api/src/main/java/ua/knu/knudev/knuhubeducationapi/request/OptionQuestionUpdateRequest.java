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
@Schema(description = "Request object for updating an option question. If any field is null, that mean value won`t be changed")
public record OptionQuestionUpdateRequest(

        @NotNull
        @Schema(
                description = "Id of the option question which will be updated",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        UUID questionId,

        @Schema(
                description = "Id of the test which will own updated question"
        )
        UUID testId,

        @Size(max = 5000)
        @Schema(
                description = "Text attached to question. Question must have at least one image or some text",
                maxLength = 5000
        )
        String text,

        @Schema(
                description = "Option question type (ONE_ANSWER or MULTI_ANSWER)"
        )
        OptionQuestionType questionType,

        @Schema(
                description = "Max possible mark for correct answer. Number can contain a maximum of 3 digits before the decimal point and 3 after it",
                defaultValue = "1"
        )
        BigDecimal maxMark,

        @Size(min = 2, max = 20)
        @Schema(
                description = "Options of the question. There must be more than 1 option and less than 21 options"
        )
        Set<OptionCreationRequest> options,


        @Size(max = 3)
        @Schema(
                description = "Images attached to the question. Maximum 3 images. Question must have at least one image or some text"
        )
        Set<MultipartFile> images
) {
}
