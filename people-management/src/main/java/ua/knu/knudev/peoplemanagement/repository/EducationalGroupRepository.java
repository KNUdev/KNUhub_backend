package ua.knu.knudev.peoplemanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.knu.knudev.peoplemanagement.domain.EducationalGroup;

import java.util.UUID;

public interface EducationalGroupRepository extends JpaRepository<EducationalGroup, UUID> {
}
