package ua.knu.knudev.knuhubcommunication.mapper;

import org.springframework.stereotype.Component;
import ua.knu.knudev.knuhubcommunication.domain.PostEntity;
import ua.knu.knudev.knuhubcommunication.domain.PostType;
import ua.knu.knudev.knuhubcommunicationapi.dto.CreatePostRequest;
import ua.knu.knudev.knuhubcommunicationapi.dto.PostResponse;
import ua.knu.knudev.knuhubcommunicationapi.dto.PostUpdateRequest;

import java.time.OffsetDateTime;
import java.util.List;

@Component
public class PostConverter {

    public PostEntity toEntity(CreatePostRequest req) {
        if (req == null) return null;

        PostEntity e = new PostEntity();
        e.setTitle(req.getTitle());
        e.setDescription(req.getDescription());
        e.setCoverFileId(req.getCoverFileId());
        e.setStartsAt(req.getStartsAt());
        e.setEndsAt(req.getEndsAt());
        e.setLocation(req.getLocation());
        if (req.getType() != null) {
            e.setType(PostType.valueOf(req.getType())); // очікує "NEWS"/"EVENT"
        }

        // за бажанням: можна покластись на дефолти БД, але тут теж ок
        e.setCreatedAt(OffsetDateTime.now());
        e.setUpdatedAt(OffsetDateTime.now());
        return e;
    }

    public void update(PostEntity e, PostUpdateRequest req) {
        if (e == null || req == null) return;

        if (req.getTitle() != null)        e.setTitle(req.getTitle());
        if (req.getDescription() != null)  e.setDescription(req.getDescription());
        if (req.getCoverFileId() != null)  e.setCoverFileId(req.getCoverFileId());
        if (req.getStartsAt() != null)     e.setStartsAt(req.getStartsAt());
        if (req.getEndsAt() != null)       e.setEndsAt(req.getEndsAt());
        if (req.getLocation() != null)     e.setLocation(req.getLocation());
        if (req.getType() != null)         e.setType(PostType.valueOf(req.getType()));

        e.setUpdatedAt(OffsetDateTime.now());
    }

    public PostResponse toResponse(PostEntity e) {
        if (e == null) return null;
        return PostResponse.builder()
                .id(e.getId())
                .type(e.getType() == null ? null : e.getType().name())
                .title(e.getTitle())
                .description(e.getDescription())
                .coverFileId(e.getCoverFileId())
                .startsAt(e.getStartsAt())
                .endsAt(e.getEndsAt())
                .location(e.getLocation())
                .createdAt(e.getCreatedAt())
                .updatedAt(e.getUpdatedAt())
                .build();
    }

    public List<PostResponse> toResponseList(List<PostEntity> entities) {
        return entities == null ? List.of() : entities.stream().map(this::toResponse).toList();
    }
}
