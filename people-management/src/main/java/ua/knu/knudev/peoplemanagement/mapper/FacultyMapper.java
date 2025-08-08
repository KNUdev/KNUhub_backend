package ua.knu.knudev.peoplemanagement.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import ua.knu.knudev.knuhubcommon.mapper.BaseMapper;
import ua.knu.knudev.peoplemanagement.domain.Faculty;
import ua.knu.knudev.peoplemanagementapi.dto.FacultyDto;

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