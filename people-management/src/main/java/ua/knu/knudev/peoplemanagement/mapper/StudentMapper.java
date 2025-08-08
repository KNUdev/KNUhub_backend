package ua.knu.knudev.peoplemanagement.mapper;

import org.mapstruct.Mapper;
import ua.knu.knudev.knuhubcommon.mapper.BaseMapper;
import ua.knu.knudev.peoplemanagement.domain.Student;
import ua.knu.knudev.peoplemanagementapi.dto.StudentDto;

@Mapper(componentModel = "spring")
public interface StudentMapper extends BaseMapper<Student, StudentDto> {
}