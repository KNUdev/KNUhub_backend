package ua.knu.knudev.peoplemanagement.mapper;

import org.mapstruct.Mapper;
import ua.knu.knudev.knuhubcommon.mapper.BaseMapper;
import ua.knu.knudev.peoplemanagement.domain.Teacher;
import ua.knu.knudev.peoplemanagementapi.dto.TeacherLiteDto;

@Mapper(componentModel = "spring")
public interface TeacherLiteMapper extends BaseMapper<Teacher, TeacherLiteDto> {
}
