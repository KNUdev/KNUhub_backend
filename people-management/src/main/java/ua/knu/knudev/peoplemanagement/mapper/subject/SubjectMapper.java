package ua.knu.knudev.peoplemanagement.mapper.subject;

import org.mapstruct.Mapper;
import ua.knu.knudev.knuhubcommon.mapper.BaseMapper;
import ua.knu.knudev.peoplemanagement.domain.Subject;
import ua.knu.knudev.peoplemanagementapi.dto.subject.SubjectDto;

@Mapper(componentModel = "spring")
public interface SubjectMapper extends BaseMapper<Subject, SubjectDto> {
}
