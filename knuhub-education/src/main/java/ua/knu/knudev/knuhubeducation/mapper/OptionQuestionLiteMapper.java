package ua.knu.knudev.knuhubeducation.mapper;

import org.mapstruct.Mapper;
import ua.knu.knudev.knuhubcommon.mapper.BaseMapper;
import ua.knu.knudev.knuhubeducation.domain.OptionQuestion;
import ua.knu.knudev.knuhubeducationapi.dto.OptionQuestionLiteDto;

@Mapper(componentModel = "spring",
        uses = {
                OptionLiteMapper.class,
                TestPreviewMapper.class,
                ImageLiteMapper.class
        })
public interface OptionQuestionLiteMapper extends BaseMapper<OptionQuestion, OptionQuestionLiteDto> {
}
