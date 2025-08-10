package ua.knu.knudev.knuhubcommunication.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.knu.knudev.knuhubcommunication.domain.PostEntity;

import java.util.UUID;

public interface PostRepository extends JpaRepository<PostEntity, UUID> {}
