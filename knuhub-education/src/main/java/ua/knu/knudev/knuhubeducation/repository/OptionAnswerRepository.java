package ua.knu.knudev.knuhubeducation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.knu.knudev.knuhubeducation.domain.OptionAnswer;

import java.util.UUID;

public interface OptionAnswerRepository extends JpaRepository<OptionAnswer, UUID> {
}
