package ua.knu.knudev.peoplemanagement.mapper;

import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Named;
import ua.knu.knudev.knuhubcommon.mapper.BaseMapper;
import ua.knu.knudev.knuhubcommon.mapper.CycleAvoidingMappingContext;
import ua.knu.knudev.peoplemanagement.domain.Teacher;
import ua.knu.knudev.peoplemanagementapi.dto.TeacherDto;

@Mapper(componentModel = "spring")
public interface TeacherMapper extends BaseMapper<Teacher, TeacherDto> {

    @Named("toDtoWithContext")
    @Override
    TeacherDto toDto(Teacher teacher, @Context CycleAvoidingMappingContext context);

    @Named("toDomainWithContext")
    @Override
    Teacher toDomain(TeacherDto dto, @Context CycleAvoidingMappingContext context);
}