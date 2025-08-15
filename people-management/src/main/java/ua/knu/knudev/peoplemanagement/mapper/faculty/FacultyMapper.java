package ua.knu.knudev.peoplemanagement.mapper.faculty;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import ua.knu.knudev.knuhubcommon.mapper.BaseMapper;
import ua.knu.knudev.peoplemanagement.domain.Faculty;
import ua.knu.knudev.peoplemanagement.mapper.educationalSpecialty.EducationalSpecialtyLiteMapper;
import ua.knu.knudev.peoplemanagement.mapper.student.StudentMapper;
import ua.knu.knudev.peoplemanagement.mapper.subject.SubjectMapper;
import ua.knu.knudev.peoplemanagement.mapper.teacher.TeacherMapper;
import ua.knu.knudev.peoplemanagement.mapper.teachingAssigment.TeachingAssigmentMapper;
import ua.knu.knudev.peoplemanagement.mapper.user.UserMapper;
import ua.knu.knudev.peoplemanagementapi.dto.faculty.FacultyDto;

@Mapper(componentModel = "spring", uses = {EducationalSpecialtyLiteMapper.class, StudentMapper.class, TeacherMapper.class,
        SubjectMapper.class, TeachingAssigmentMapper.class, UserMapper.class})
public interface FacultyMapper extends BaseMapper<Faculty, FacultyDto> {

    @Mappings({
            @Mapping(source = "name.uk", target = "facultyName.uk"),
            @Mapping(source = "name.en", target = "facultyName.en")
    })
    FacultyDto toDto(Faculty faculty);

    @Mappings({
            @Mapping(source = "facultyName.uk", target = "name.uk"),
            @Mapping(source = "facultyName.en", target = "name.en")
    })
    Faculty toDomain(FacultyDto facultyDto);

}