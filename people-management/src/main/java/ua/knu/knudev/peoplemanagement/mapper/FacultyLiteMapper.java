package ua.knu.knudev.peoplemanagement.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import ua.knu.knudev.knuhubcommon.mapper.BaseMapper;
import ua.knu.knudev.peoplemanagement.domain.Faculty;
import ua.knu.knudev.peoplemanagementapi.dto.FacultyLiteDto;

@Mapper(componentModel = "spring")
public interface FacultyLiteMapper extends BaseMapper<Faculty, FacultyLiteDto> {

    @Mappings({
            @Mapping(source = "name.uk", target = "name.uk"),
            @Mapping(source = "name.en", target = "name.en")
    })
    FacultyLiteDto toDto(Faculty faculty);

    @Mappings({
            @Mapping(source = "name.uk", target = "name.uk"),
            @Mapping(source = "name.en", target = "name.en")
    })
    Faculty toDomain(FacultyLiteDto facultyLiteDto);
}
