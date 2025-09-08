package ua.knu.knudev.knuhubeducation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.knu.knudev.knuhubeducation.domain.matching.MatchQuestion;

import java.util.UUID;

public interface MatchQuestionRepository extends JpaRepository<MatchQuestion, UUID> {
}
