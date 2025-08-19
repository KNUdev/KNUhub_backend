package ua.knu.knudev.peoplemanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.knu.knudev.peoplemanagement.domain.Student;

import java.util.UUID;

public interface StudentRepository extends JpaRepository<Student, UUID> {
}
