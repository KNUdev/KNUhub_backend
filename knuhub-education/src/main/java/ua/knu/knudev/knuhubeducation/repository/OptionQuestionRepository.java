package ua.knu.knudev.knuhubeducation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.knu.knudev.knuhubeducation.domain.OptionQuestion;

import java.util.UUID;

public interface OptionQuestionRepository extends JpaRepository<OptionQuestion, UUID> {
}
