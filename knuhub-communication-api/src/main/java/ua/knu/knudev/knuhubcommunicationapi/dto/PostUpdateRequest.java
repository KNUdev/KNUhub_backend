package ua.knu.knudev.knuhubcommunicationapi.dto;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.OffsetDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostUpdateRequest {

    @Pattern(regexp = "NEWS|EVENT")
    private String type;

    @Size(max = 255)
    private String title;

    private String description;

    @Size(max = 128)
    private String coverFileId;

    private OffsetDateTime startsAt;
    private OffsetDateTime endsAt;

    @Size(max = 255)
    private String location;
}
