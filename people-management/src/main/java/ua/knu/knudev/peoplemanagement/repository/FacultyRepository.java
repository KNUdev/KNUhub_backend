package ua.knu.knudev.peoplemanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.knu.knudev.peoplemanagement.domain.Faculty;

import java.util.UUID;

public interface FacultyRepository extends JpaRepository<Faculty, UUID> {
}
