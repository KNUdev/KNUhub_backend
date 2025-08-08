package ua.knu.knudev.peoplemanagement.mapper;

import org.mapstruct.Mapper;
import ua.knu.knudev.knuhubcommon.mapper.BaseMapper;
import ua.knu.knudev.peoplemanagement.domain.EducationalGroup;
import ua.knu.knudev.peoplemanagementapi.dto.EducationalGroupDto;

@Mapper(componentModel = "spring", uses = {
        StudentMapper.class,
        TeacherMapper.class,
        SubjectMapper.class,
        TeachingAssigmentMapper.class
})
public interface EducationalGroupMapper extends BaseMapper<EducationalGroupDto, EducationalGroup> {
}