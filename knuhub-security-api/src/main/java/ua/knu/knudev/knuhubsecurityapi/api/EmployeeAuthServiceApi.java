package ua.knu.knudev.knuhubsecurityapi.api;

import ua.knu.knudev.knuhubsecurityapi.dto.AuthenticatedEmployeeDto;
import ua.knu.knudev.knuhubsecurityapi.request.AuthenticatedEmployeeUpdateRequest;
import ua.knu.knudev.knuhubsecurityapi.request.EmployeeRegistrationRequest;
import ua.knu.knudev.knuhubsecurityapi.response.EmployeeRegistrationResponse;

public interface EmployeeAuthServiceApi {

    EmployeeRegistrationResponse create(EmployeeRegistrationRequest request);

    boolean existsByEmail(String email);

    AuthenticatedEmployeeDto getByEmail(String email);

    void deleteByEmail(String email);

    void update(AuthenticatedEmployeeUpdateRequest request);


}
