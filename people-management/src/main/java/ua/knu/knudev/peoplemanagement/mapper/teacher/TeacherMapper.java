package ua.knu.knudev.peoplemanagement.mapper.teacher;

import org.mapstruct.Mapper;
import ua.knu.knudev.knuhubcommon.mapper.BaseMapper;
import ua.knu.knudev.peoplemanagement.domain.Teacher;
import ua.knu.knudev.peoplemanagementapi.dto.teacher.TeacherDto;

@Mapper(componentModel = "spring")
public interface TeacherMapper extends BaseMapper<Teacher, TeacherDto> {
}