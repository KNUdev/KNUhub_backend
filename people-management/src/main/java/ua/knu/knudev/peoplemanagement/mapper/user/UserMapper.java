package ua.knu.knudev.peoplemanagement.mapper.user;

import org.mapstruct.Mapper;
import ua.knu.knudev.knuhubcommon.mapper.BaseMapper;
import ua.knu.knudev.peoplemanagement.domain.User;
import ua.knu.knudev.peoplemanagement.mapper.student.StudentMapper;
import ua.knu.knudev.peoplemanagement.mapper.subject.SubjectMapper;
import ua.knu.knudev.peoplemanagement.mapper.teacher.TeacherMapper;
import ua.knu.knudev.peoplemanagement.mapper.teachingAssigment.TeachingAssigmentMapper;
import ua.knu.knudev.peoplemanagementapi.dto.user.UserDto;

@Mapper(componentModel = "spring", uses = {StudentMapper.class, TeacherMapper.class, SubjectMapper.class,
        TeachingAssigmentMapper.class})
public interface UserMapper extends BaseMapper<User, UserDto> {
}