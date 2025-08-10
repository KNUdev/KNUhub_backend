package ua.knu.knudev.knuhubcommunicationapi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.time.OffsetDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreatePostRequest {
    @NotNull
    @Pattern(regexp = "NEWS|EVENT")
    private String type;

    @NotBlank
    @Size(max = 255)
    private String title;

    @NotBlank
    private String description;

    @Size(max = 128)
    private String coverFileId;

    private OffsetDateTime startsAt;
    private OffsetDateTime endsAt;

    @Size(max = 255)
    private String location;
}
