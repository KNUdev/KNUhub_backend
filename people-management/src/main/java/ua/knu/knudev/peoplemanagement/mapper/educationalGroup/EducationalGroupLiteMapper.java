package ua.knu.knudev.peoplemanagement.mapper.educationalGroup;

import org.mapstruct.Mapper;
import ua.knu.knudev.knuhubcommon.mapper.BaseMapper;
import ua.knu.knudev.peoplemanagement.domain.EducationalGroup;
import ua.knu.knudev.peoplemanagementapi.dto.educationalGroup.EducationalGroupLiteDto;

@Mapper(componentModel = "spring")
public interface EducationalGroupLiteMapper extends BaseMapper<EducationalGroup, EducationalGroupLiteDto> {
}
