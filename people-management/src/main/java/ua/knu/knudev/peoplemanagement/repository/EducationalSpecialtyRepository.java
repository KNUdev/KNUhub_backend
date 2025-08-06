package ua.knu.knudev.peoplemanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.knu.knudev.peoplemanagement.domain.EducationalSpecialty;

public interface EducationalSpecialtyRepository extends JpaRepository<EducationalSpecialty, String> {
}
