package ua.knu.knudev.knuhubeducation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.knu.knudev.knuhubeducation.domain.TextQuestion;

import java.util.UUID;

public interface TextQuestionRepository extends JpaRepository<TextQuestion, UUID> {
}
