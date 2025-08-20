package ua.knu.knudev.knuhubeducationapi.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import org.springframework.web.multipart.MultipartFile;
import ua.knu.knudev.knuhubcommon.constant.AnswersRevealTime;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Builder
@Schema(description = "Request object for creating a test")
public record TestCreationRequest(

        @NotBlank(message = "Test 'title' can not be blank")
        @Size(max = 200)
        @Schema(
                description = "Test title",
                requiredMode = Schema.RequiredMode.REQUIRED,
                maxLength = 200)
        String title,

        @NotBlank(message = "Test 'description' can not be blank")
        @Size(max = 5000)
        @Schema(
                description = "Test description",
                requiredMode = Schema.RequiredMode.REQUIRED,
                maxLength = 5000
        )
        String description,

        @NotNull(message = "Test 'isProtectedMode' can not be null")
        @Schema(
                description = "Mode which will be used when taking the test. " +
                        "If true, test taker will be limited in some actions",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        Boolean isProtectedMode,

        @NotNull(message = "Test 'answersRevealTime' can not be null")
        @Schema(
                description = "Affects the time when correct answers can be accessed by test takers",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        AnswersRevealTime answersRevealTime,

        @Schema(
                description = "Time when test will be closed"
        )
        LocalDateTime deadline,

        @NotNull(message = "Test 'durationMinutes' can not be null")
        @Min(1)
        @Schema(
                description = "The number of minutes the test will be available after test taker enters it",
                requiredMode = Schema.RequiredMode.REQUIRED,
                minimum = "1"
        )
        Integer durationMinutes,

        @NotNull(message = "Test 'creatorId' can not be null")
        @Schema(
                description = "Id of a user who created the test",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        UUID creatorId,

        @Size(max = 3)
        @Schema(
                description = "Image files which will be attached to the test. Maximum 3 images"
        )
        Set<MultipartFile> images
) {
}
