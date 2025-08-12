package ua.knu.knudev.peoplemanagement.mapper;

import org.mapstruct.Mapper;
import ua.knu.knudev.knuhubcommon.mapper.BaseMapper;
import ua.knu.knudev.peoplemanagement.domain.Subject;
import ua.knu.knudev.peoplemanagementapi.dto.SubjectLiteDto;

@Mapper(componentModel = "spring")
public interface SubjectLiteMapper extends BaseMapper<Subject, SubjectLiteDto> {
}
