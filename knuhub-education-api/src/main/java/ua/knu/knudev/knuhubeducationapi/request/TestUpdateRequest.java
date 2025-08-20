package ua.knu.knudev.knuhubeducationapi.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import org.springframework.web.multipart.MultipartFile;
import ua.knu.knudev.knuhubcommon.constant.AnswersRevealTime;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Builder
@Schema(description = "Request object for updating a test. If any field is null, that mean value won`t be changed")
public record TestUpdateRequest(

        @NotNull(message = "'testId' can not be null")
        @Schema(
                description = "Id of the test that needs to be updated",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        UUID testId,

        @Size(max = 200)
        @Schema(
                description = "Test title",
                maxLength = 200)
        String title,

        @Size(max = 5000)
        @Schema(
                description = "Test description",
                maxLength = 5000
        )
        String description,

        @Schema(
                description = "Mode which will be used when taking the test. " +
                        "If true, test taker will be limited in some actions"
        )
        Boolean isProtectedMode,

        @Schema(
                description = "Affects the time when correct answers can be accessed by test takers"
        )
        AnswersRevealTime answersRevealTime,

        @Schema(
                description = "Time when test will be closed"
        )
        LocalDateTime deadline,

        @Min(1)
        @Schema(
                description = "The number of minutes the test will be available after test taker enters it",
                minimum = "1"
        )
        Integer durationMinutes,

        @Schema(
                description = "Id of a user who created the test"
        )
        UUID creatorId,

        @Size(max = 3)
        @Schema(
                description = "Image files which will be attached to the test. Maximum 3 images. " +
                        "If not null, previous images will be deleted"
        )
        Set<MultipartFile> images
) {
}
