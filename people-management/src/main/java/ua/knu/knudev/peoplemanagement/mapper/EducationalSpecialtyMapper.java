package ua.knu.knudev.peoplemanagement.mapper;

import org.mapstruct.Mapper;
import ua.knu.knudev.knuhubcommon.mapper.BaseMapper;
import ua.knu.knudev.peoplemanagement.domain.EducationalSpecialty;
import ua.knu.knudev.peoplemanagementapi.dto.EducationalSpecialtyDto;

@Mapper(componentModel = "spring", uses = {EducationalGroupMapper.class, StudentMapper.class,
        TeacherMapper.class, TeachingAssigmentMapper.class, UserMapper.class, FacultyLiteMapper.class})
public interface EducationalSpecialtyMapper extends BaseMapper<EducationalSpecialty, EducationalSpecialtyDto> {
}