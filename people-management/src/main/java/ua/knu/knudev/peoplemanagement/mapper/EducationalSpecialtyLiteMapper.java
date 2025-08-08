package ua.knu.knudev.peoplemanagement.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import ua.knu.knudev.knuhubcommon.mapper.BaseMapper;
import ua.knu.knudev.peoplemanagement.domain.EducationalSpecialty;
import ua.knu.knudev.peoplemanagementapi.dto.EducationalSpecialtyLiteDto;

@Mapper(componentModel = "spring")
public interface EducationalSpecialtyLiteMapper extends BaseMapper<EducationalSpecialty, EducationalSpecialtyLiteDto> {

    @Mappings({
            @Mapping(source = "name.uk", target = "name.uk"),
            @Mapping(source = "name.en", target = "name.en")
    })
    EducationalSpecialtyLiteDto toDto(EducationalSpecialty educationalSpecialty);

    @Mappings({
            @Mapping(source = "name.uk", target = "name.uk"),
            @Mapping(source = "name.en", target = "name.en")
    })
    EducationalSpecialty toDomain(EducationalSpecialtyLiteDto educationalSpecialtyLiteDto);
}
