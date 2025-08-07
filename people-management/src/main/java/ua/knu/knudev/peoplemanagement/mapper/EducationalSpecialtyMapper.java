package ua.knu.knudev.peoplemanagement.mapper;

import org.mapstruct.*;
import ua.knu.knudev.knuhubcommon.mapper.BaseMapper;
import ua.knu.knudev.knuhubcommon.mapper.CycleAvoidingMappingContext;
import ua.knu.knudev.peoplemanagement.domain.EducationalSpecialty;
import ua.knu.knudev.peoplemanagementapi.dto.EducationalSpecialtyDto;

@Mapper(componentModel = "spring", uses = {EducationalGroupMapper.class, StudentMapper.class,
        TeacherMapper.class, TeachingAssigmentMapper.class, UserMapper.class, FacultyMapper.class})
public interface EducationalSpecialtyMapper extends BaseMapper<EducationalSpecialty, EducationalSpecialtyDto> {

    @Named("toDtoWithContext")
    @Override
    default EducationalSpecialtyDto toDto(EducationalSpecialty educationalSpecialty, @Context CycleAvoidingMappingContext context) {
        if (educationalSpecialty == null) return null;

        EducationalSpecialtyDto existing = context.getMappedInstance(educationalSpecialty, EducationalSpecialtyDto.class);
        if (existing != null) return existing;

        return mapToDto(educationalSpecialty, context);
    }

    @Named("toDomainWithContext")
    @Mappings({
            @Mapping(target = "groups", source = "groups", qualifiedByName = "toDomainWithContext"),
            @Mapping(target = "students", source = "students", qualifiedByName = "toDomainWithContext"),
            @Mapping(target = "teachers", source = "teachers", qualifiedByName = "toDomainWithContext"),
            @Mapping(target = "teachingAssigments", source = "teachingAssigments", qualifiedByName = "toDomainWithContext"),
            @Mapping(target = "faculties", source = "faculties", qualifiedByName = "toDomainWithContext")
    })
    @Override
    EducationalSpecialty toDomain(EducationalSpecialtyDto dto, @Context CycleAvoidingMappingContext context);

    @Mappings({
            @Mapping(target = "groups", source = "groups", qualifiedByName = "toDtoWithContext"),
            @Mapping(target = "students", source = "students", qualifiedByName = "toDtoWithContext"),
            @Mapping(target = "teachers", source = "teachers", qualifiedByName = "toDtoWithContext"),
            @Mapping(target = "teachingAssigments", source = "teachingAssigments", qualifiedByName = "toDtoWithContext"),
            @Mapping(target = "faculties", source = "faculties", qualifiedByName = "toDtoWithContext")
    })
    EducationalSpecialtyDto mapToDto(EducationalSpecialty educationalSpecialty, @Context CycleAvoidingMappingContext context);

    @AfterMapping
    default void afterMapping(
            @MappingTarget EducationalSpecialtyDto target,
            EducationalSpecialty source,
            @Context CycleAvoidingMappingContext context
    ) {
        context.storeMappedInstance(source, target);
    }
}