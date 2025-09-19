package ua.knu.knudev.knuhubeducation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.knu.knudev.knuhubeducation.domain.TestAttempt;

import java.util.UUID;

public interface TestAttemptRepository extends JpaRepository<TestAttempt, UUID> {
}
