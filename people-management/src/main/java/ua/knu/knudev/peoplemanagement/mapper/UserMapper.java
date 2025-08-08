package ua.knu.knudev.peoplemanagement.mapper;

import org.mapstruct.Mapper;
import ua.knu.knudev.knuhubcommon.mapper.BaseMapper;
import ua.knu.knudev.peoplemanagement.domain.User;
import ua.knu.knudev.peoplemanagementapi.dto.UserDto;

@Mapper(componentModel = "spring", uses = {StudentMapper.class, TeacherMapper.class, SubjectMapper.class,
        TeachingAssigmentMapper.class})
public interface UserMapper extends BaseMapper<User, UserDto> {
}