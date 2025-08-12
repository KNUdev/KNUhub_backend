package ua.knu.knudev.peoplemanagement.mapper;

import org.mapstruct.Mapper;
import ua.knu.knudev.knuhubcommon.mapper.BaseMapper;
import ua.knu.knudev.peoplemanagement.domain.EducationalSpecialty;
import ua.knu.knudev.peoplemanagementapi.dto.EducationalSpecialtyDto;

@Mapper(componentModel = "spring", uses = {EducationalGroupLiteMapper.class, StudentLiteMapper.class,
        TeacherLiteMapper.class, TeachingAssigmentLiteMapper.class, UserLiteMapper.class, FacultyLiteMapper.class})
public interface EducationalSpecialtyMapper extends BaseMapper<EducationalSpecialty, EducationalSpecialtyDto> {
}