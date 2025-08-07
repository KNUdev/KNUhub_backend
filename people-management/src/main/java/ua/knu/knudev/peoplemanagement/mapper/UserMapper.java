package ua.knu.knudev.peoplemanagement.mapper;

import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Named;
import ua.knu.knudev.knuhubcommon.mapper.BaseMapper;
import ua.knu.knudev.knuhubcommon.mapper.CycleAvoidingMappingContext;
import ua.knu.knudev.peoplemanagement.domain.User;
import ua.knu.knudev.peoplemanagementapi.dto.UserDto;

@Mapper(componentModel = "spring", uses = {StudentMapper.class, TeacherMapper.class, SubjectMapper.class,
        TeachingAssigmentMapper.class})
public interface UserMapper extends BaseMapper<User, UserDto> {

    @Named("toDtoWithContext")
    @Override
    UserDto toDto(User user, @Context CycleAvoidingMappingContext context);

    @Named("toDomainWithContext")
    @Override
    User toDomain(UserDto dto, @Context CycleAvoidingMappingContext context);
}