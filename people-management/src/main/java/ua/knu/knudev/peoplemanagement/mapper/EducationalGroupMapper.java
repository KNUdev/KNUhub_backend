package ua.knu.knudev.peoplemanagement.mapper;

import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import ua.knu.knudev.knuhubcommon.mapper.BaseMapper;
import ua.knu.knudev.knuhubcommon.mapper.CycleAvoidingMappingContext;
import ua.knu.knudev.peoplemanagement.domain.EducationalGroup;
import ua.knu.knudev.peoplemanagementapi.dto.EducationalGroupDto;

@Mapper(componentModel = "spring", uses = {
        StudentMapper.class,
        TeacherMapper.class,
        SubjectMapper.class,
        TeachingAssigmentMapper.class
})
public abstract class EducationalGroupMapper implements BaseMapper<EducationalGroup, EducationalGroupDto> {

    @Lazy
    @Autowired
    protected FacultyMapper facultyMapper;

    @Lazy
    @Autowired
    protected EducationalSpecialtyMapper educationalSpecialtyMapper;

    @Named("toDtoWithContext")
    @Override
    public EducationalGroupDto toDto(EducationalGroup educationalGroup, @Context CycleAvoidingMappingContext context) {
        if (educationalGroup == null) return null;

        EducationalGroupDto existing = context.getMappedInstance(educationalGroup, EducationalGroupDto.class);
        if (existing != null) return existing;

        return mapToDto(educationalGroup, context);
    }

    @Named("toDomainWithContext")
    @Mappings({
            @Mapping(target = "faculties", expression = "java(facultyMapper.toDomains(dto.faculties(), context))"),
            @Mapping(target = "students", source = "students", qualifiedByName = "toDomainWithContext"),
            @Mapping(target = "teachers", source = "teachers", qualifiedByName = "toDomainWithContext"),
            @Mapping(target = "subjects", source = "subjects", qualifiedByName = "toDomainWithContext"),
            @Mapping(target = "educationalSpecialties", expression = "java(educationalSpecialtyMapper.toDomains(dto.educationalSpecialties(), context))")
    })
    @Override
    public abstract EducationalGroup toDomain(EducationalGroupDto dto, @Context CycleAvoidingMappingContext context);

    @Mappings({
            @Mapping(target = "faculties", expression = "java(facultyMapper.toDtos(educationalGroup.getFaculties(), context))"),
            @Mapping(target = "students", source = "students", qualifiedByName = "toDtoWithContext"),
            @Mapping(target = "teachers", source = "teachers", qualifiedByName = "toDtoWithContext"),
            @Mapping(target = "subjects", source = "subjects", qualifiedByName = "toDtoWithContext"),
            @Mapping(target = "educationalSpecialties", expression = "java(educationalSpecialtyMapper.toDtos(educationalGroup.getEducationalSpecialties(), context))")
    })
    public abstract EducationalGroupDto mapToDto(EducationalGroup educationalGroup, @Context CycleAvoidingMappingContext context);

    @AfterMapping
    protected void afterMapping(
            @MappingTarget EducationalGroupDto target,
            EducationalGroup source,
            @Context CycleAvoidingMappingContext context
    ) {
        context.storeMappedInstance(source, target);
    }
}