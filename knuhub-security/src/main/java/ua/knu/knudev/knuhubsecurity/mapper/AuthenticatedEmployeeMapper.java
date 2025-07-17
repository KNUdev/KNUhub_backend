package ua.knu.knudev.knuhubsecurity.mapper;

import org.mapstruct.Mapper;
import ua.knu.knudev.knuhubcommon.mapper.BaseMapper;
import ua.knu.knudev.knuhubsecurity.domain.AuthenticatedEmployee;
import ua.knu.knudev.knuhubsecurityapi.dto.AuthenticatedEmployeeDto;

@Mapper(componentModel = "spring")
public interface AuthenticatedEmployeeMapper extends BaseMapper<AuthenticatedEmployee, AuthenticatedEmployeeDto> {
}
