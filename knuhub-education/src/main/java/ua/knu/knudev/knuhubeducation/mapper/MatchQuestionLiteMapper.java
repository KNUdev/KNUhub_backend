package ua.knu.knudev.knuhubeducation.mapper;

import org.mapstruct.Mapper;
import ua.knu.knudev.knuhubcommon.mapper.BaseMapper;
import ua.knu.knudev.knuhubeducation.domain.matching.MatchQuestion;
import ua.knu.knudev.knuhubeducationapi.dto.MatchQuestionLiteDto;

@Mapper(componentModel = "spring",
        uses = {
                OptionLiteMapper.class,
                TestPreviewMapper.class,
                ImageLiteMapper.class
        })
public interface MatchQuestionLiteMapper extends BaseMapper<MatchQuestion, MatchQuestionLiteDto> {
}
