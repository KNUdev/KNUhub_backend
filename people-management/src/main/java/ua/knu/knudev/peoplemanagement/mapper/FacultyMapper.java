package ua.knu.knudev.peoplemanagement.mapper;

import org.mapstruct.*;
import ua.knu.knudev.knuhubcommon.mapper.BaseMapper;
import ua.knu.knudev.knuhubcommon.mapper.CycleAvoidingMappingContext;
import ua.knu.knudev.peoplemanagement.domain.Faculty;
import ua.knu.knudev.peoplemanagementapi.dto.FacultyDto;

@Mapper(componentModel = "spring", uses = {EducationalSpecialtyMapper.class, EducationalGroupMapper.class,
        StudentMapper.class, TeacherMapper.class, SubjectMapper.class, TeachingAssigmentMapper.class, UserMapper.class})
public interface FacultyMapper extends BaseMapper<Faculty, FacultyDto> {

    @Named("toDtoWithContext")
    @Mappings({
            @Mapping(target = "facultyName.en", source = "name.en"),
            @Mapping(target = "facultyName.uk", source = "name.uk"),
            @Mapping(target = "educationalSpecialties", source = "educationalSpecialties", qualifiedByName = "toDtoWithContext"),
            @Mapping(target = "educationalGroups", source = "educationalGroups", qualifiedByName = "toDtoWithContext"),
            @Mapping(target = "users", source = "users", qualifiedByName = "toDtoWithContext")
    })
    @Override
    FacultyDto toDto(Faculty faculty, @Context CycleAvoidingMappingContext context);

    @Named("toDomainWithContext")
    @Mappings({
            @Mapping(target = "name.en", source = "facultyName.en"),
            @Mapping(target = "name.uk", source = "facultyName.uk"),
            @Mapping(target = "educationalSpecialties", source = "educationalSpecialties", qualifiedByName = "toDomainWithContext"),
            @Mapping(target = "educationalGroups", source = "educationalGroups", qualifiedByName = "toDomainWithContext"),
            @Mapping(target = "users", source = "users", qualifiedByName = "toDomainWithContext")
    })
    @Override
    Faculty toDomain(FacultyDto facultyDto, @Context CycleAvoidingMappingContext context);

    @AfterMapping
    default void afterMappingToDto(@MappingTarget FacultyDto target, Faculty source, @Context CycleAvoidingMappingContext context) {
        context.storeMappedInstance(source, target);
    }

    @AfterMapping
    default void afterMappingToDomain(@MappingTarget Faculty target, FacultyDto source, @Context CycleAvoidingMappingContext context) {
        context.storeMappedInstance(source, target);
    }
}