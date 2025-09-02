package ua.knu.knudev.peoplemanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.knu.knudev.peoplemanagement.domain.TeachingAssigment;

import java.util.UUID;

public interface TeachingAssigmentRepository extends JpaRepository<TeachingAssigment, UUID> {
}
