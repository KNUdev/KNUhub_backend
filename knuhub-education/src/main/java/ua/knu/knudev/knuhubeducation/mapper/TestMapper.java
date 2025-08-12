package ua.knu.knudev.knuhubeducation.mapper;

import org.mapstruct.Mapper;
import ua.knu.knudev.knuhubcommon.mapper.BaseMapper;
import ua.knu.knudev.knuhubeducation.domain.TestDomain;
import ua.knu.knudev.knuhubeducationapi.dto.TestDto;

@Mapper(componentModel = "spring")
public interface TestMapper extends BaseMapper<TestDomain, TestDto> {
}
