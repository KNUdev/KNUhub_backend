package ua.knu.knudev.knuhubeducation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.knu.knudev.knuhubeducation.domain.matching.MatchAnswer;

import java.util.UUID;

public interface MatchAnswerRepository extends JpaRepository<MatchAnswer, UUID> {
}
