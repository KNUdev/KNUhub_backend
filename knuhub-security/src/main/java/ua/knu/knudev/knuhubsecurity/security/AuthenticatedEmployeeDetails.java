package ua.knu.knudev.knuhubsecurity.security;

import org.springframework.security.core.userdetails.UserDetails;
import ua.knu.knudev.knuhubcommon.constant.UserRole;

import java.util.Set;
import java.util.UUID;

public interface AuthenticatedEmployeeDetails extends UserDetails {

    UUID getId();

    String getEmail();

    Set<UserRole> getRoles();

}
