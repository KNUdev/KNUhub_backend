package ua.knu.knudev.knuhubeducation.mapper;

import org.mapstruct.Mapper;
import ua.knu.knudev.knuhubcommon.mapper.BaseMapper;
import ua.knu.knudev.knuhubeducation.domain.TestAttempt;
import ua.knu.knudev.knuhubeducationapi.dto.TestAttemptDto;

@Mapper(componentModel = "spring")
public interface TestAttemptMapper extends BaseMapper<TestAttempt, TestAttemptDto> {
}
