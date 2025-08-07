package ua.knu.knudev.peoplemanagement.mapper;

import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Named;
import ua.knu.knudev.knuhubcommon.mapper.BaseMapper;
import ua.knu.knudev.knuhubcommon.mapper.CycleAvoidingMappingContext;
import ua.knu.knudev.peoplemanagement.domain.Student;
import ua.knu.knudev.peoplemanagementapi.dto.StudentDto;

@Mapper(componentModel = "spring")
public interface StudentMapper extends BaseMapper<Student, StudentDto> {

    @Named("toDtoWithContext")
    @Override
    StudentDto toDto(Student student, @Context CycleAvoidingMappingContext context);

    @Named("toDomainWithContext")
    @Override
    Student toDomain(StudentDto dto, @Context CycleAvoidingMappingContext context);
}