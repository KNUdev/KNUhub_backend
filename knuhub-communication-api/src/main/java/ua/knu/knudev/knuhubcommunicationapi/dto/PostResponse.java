package ua.knu.knudev.knuhubcommunicationapi.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostResponse {
    private UUID id;
    private String type;
    private String title;
    private String description;
    private String coverFileId;
    private OffsetDateTime startsAt;
    private OffsetDateTime endsAt;
    private String location;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
}
