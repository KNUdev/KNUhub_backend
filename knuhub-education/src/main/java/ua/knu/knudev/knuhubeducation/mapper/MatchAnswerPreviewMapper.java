package ua.knu.knudev.knuhubeducation.mapper;

import org.mapstruct.Mapper;
import ua.knu.knudev.knuhubcommon.mapper.BaseMapper;
import ua.knu.knudev.knuhubeducation.domain.matching.MatchAnswer;
import ua.knu.knudev.knuhubeducationapi.dto.match.MatchAnswerPreviewDto;

@Mapper(componentModel = "spring")
public interface MatchAnswerPreviewMapper extends BaseMapper<MatchAnswer, MatchAnswerPreviewDto> {
}
