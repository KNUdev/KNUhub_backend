package ua.knu.knudev.peoplemanagement.mapper.educationalGroup;

import org.mapstruct.Mapper;
import ua.knu.knudev.knuhubcommon.mapper.BaseMapper;
import ua.knu.knudev.peoplemanagement.domain.EducationalGroup;
import ua.knu.knudev.peoplemanagement.mapper.educationalSpecialty.EducationalSpecialtyLiteMapper;
import ua.knu.knudev.peoplemanagement.mapper.student.StudentLiteMapper;
import ua.knu.knudev.peoplemanagement.mapper.student.StudentMapper;
import ua.knu.knudev.peoplemanagement.mapper.subject.SubjectLiteMapper;
import ua.knu.knudev.peoplemanagement.mapper.subject.SubjectMapper;
import ua.knu.knudev.peoplemanagement.mapper.teacher.TeacherLiteMapper;
import ua.knu.knudev.peoplemanagement.mapper.teacher.TeacherMapper;
import ua.knu.knudev.peoplemanagement.mapper.teachingAssigment.TeachingAssigmentLiteMapper;
import ua.knu.knudev.peoplemanagement.mapper.teachingAssigment.TeachingAssigmentMapper;
import ua.knu.knudev.peoplemanagementapi.dto.educationalGroup.EducationalGroupDto;

@Mapper(componentModel = "spring",
        uses = {
                StudentLiteMapper.class,
                TeacherLiteMapper.class,
                SubjectLiteMapper.class,
                TeachingAssigmentLiteMapper.class,
                EducationalSpecialtyLiteMapper.class
        })
public interface EducationalGroupMapper extends BaseMapper<EducationalGroup, EducationalGroupDto> {
}