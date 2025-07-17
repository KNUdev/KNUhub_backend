package ua.knu.knudev.knuhubsecurity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.knu.knudev.knuhubsecurity.domain.AuthenticatedEmployee;

import java.util.Optional;
import java.util.UUID;

public interface AuthenticatedEmployeeRepository extends JpaRepository<AuthenticatedEmployee, UUID> {

    Optional<AuthenticatedEmployee> findAuthenticatedEmployeesByEmail(String email);

    boolean existsByEmail(String email);

    Optional<AuthenticatedEmployee> findByEmail(String email);

}
