package ua.knu.knudev.knuhubeducation.mapper;

import org.mapstruct.Mapper;
import ua.knu.knudev.knuhubcommon.mapper.BaseMapper;
import ua.knu.knudev.knuhubeducation.domain.TestDomain;
import ua.knu.knudev.knuhubeducationapi.dto.test.TestPreviewDto;

@Mapper(componentModel = "spring",
        uses = {
                ImageLiteMapper.class
        })
public interface TestPreviewMapper extends BaseMapper<TestDomain, TestPreviewDto> {
}
