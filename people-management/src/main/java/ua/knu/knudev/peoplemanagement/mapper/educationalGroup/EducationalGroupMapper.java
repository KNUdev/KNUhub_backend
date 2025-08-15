package ua.knu.knudev.peoplemanagement.mapper.educationalGroup;

import org.mapstruct.Mapper;
import ua.knu.knudev.knuhubcommon.mapper.BaseMapper;
import ua.knu.knudev.peoplemanagement.domain.EducationalGroup;
import ua.knu.knudev.peoplemanagement.mapper.student.StudentMapper;
import ua.knu.knudev.peoplemanagement.mapper.subject.SubjectMapper;
import ua.knu.knudev.peoplemanagement.mapper.teacher.TeacherMapper;
import ua.knu.knudev.peoplemanagement.mapper.teachingAssigment.TeachingAssigmentMapper;
import ua.knu.knudev.peoplemanagementapi.dto.educationalGroup.EducationalGroupDto;

@Mapper(componentModel = "spring", uses = {
        StudentMapper.class,
        TeacherMapper.class,
        SubjectMapper.class,
        TeachingAssigmentMapper.class
})
public interface EducationalGroupMapper extends BaseMapper<EducationalGroupDto, EducationalGroup> {
}