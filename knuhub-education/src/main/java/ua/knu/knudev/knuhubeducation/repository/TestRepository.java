package ua.knu.knudev.knuhubeducation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.knu.knudev.knuhubeducation.domain.TestDomain;

import java.util.UUID;

public interface TestRepository extends JpaRepository<TestDomain, UUID> {
}
