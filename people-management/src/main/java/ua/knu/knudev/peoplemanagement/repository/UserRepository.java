package ua.knu.knudev.peoplemanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.knu.knudev.peoplemanagement.domain.User;

import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
}
