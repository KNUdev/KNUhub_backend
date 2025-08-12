package ua.knu.knudev.peoplemanagement.mapper;

import org.mapstruct.Mapper;
import ua.knu.knudev.knuhubcommon.mapper.BaseMapper;
import ua.knu.knudev.peoplemanagement.domain.EducationalGroup;
import ua.knu.knudev.peoplemanagementapi.dto.EducationalGroupLiteDto;

@Mapper(componentModel = "spring")
public interface EducationalGroupLiteMapper extends BaseMapper<EducationalGroup, EducationalGroupLiteDto> {
}
