package ua.knu.knudev.peoplemanagement.mapper.educationalSpecialty;

import org.mapstruct.Mapper;
import ua.knu.knudev.knuhubcommon.mapper.BaseMapper;
import ua.knu.knudev.peoplemanagement.domain.EducationalSpecialty;
import ua.knu.knudev.peoplemanagement.mapper.educationalGroup.EducationalGroupLiteMapper;
import ua.knu.knudev.peoplemanagement.mapper.faculty.FacultyLiteMapper;
import ua.knu.knudev.peoplemanagement.mapper.student.StudentLiteMapper;
import ua.knu.knudev.peoplemanagement.mapper.teacher.TeacherLiteMapper;
import ua.knu.knudev.peoplemanagement.mapper.teachingAssigment.TeachingAssigmentLiteMapper;
import ua.knu.knudev.peoplemanagement.mapper.user.UserLiteMapper;
import ua.knu.knudev.peoplemanagementapi.dto.educationalSpecialty.EducationalSpecialtyDto;

@Mapper(componentModel = "spring", uses = {EducationalGroupLiteMapper.class, StudentLiteMapper.class,
        TeacherLiteMapper.class, TeachingAssigmentLiteMapper.class, UserLiteMapper.class, FacultyLiteMapper.class})
public interface EducationalSpecialtyMapper extends BaseMapper<EducationalSpecialty, EducationalSpecialtyDto> {
}