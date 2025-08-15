package ua.knu.knudev.peoplemanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.knu.knudev.peoplemanagement.domain.Subject;

import java.util.UUID;

public interface SubjectRepository extends JpaRepository<Subject, UUID> {
}
