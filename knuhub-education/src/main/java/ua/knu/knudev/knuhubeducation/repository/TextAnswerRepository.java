package ua.knu.knudev.knuhubeducation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.knu.knudev.knuhubeducation.domain.TextAnswer;

import java.util.UUID;

public interface TextAnswerRepository extends JpaRepository<TextAnswer, UUID> {
}
