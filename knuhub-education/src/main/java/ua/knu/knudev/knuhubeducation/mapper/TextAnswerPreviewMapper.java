package ua.knu.knudev.knuhubeducation.mapper;

import org.mapstruct.Mapper;
import ua.knu.knudev.knuhubcommon.mapper.BaseMapper;
import ua.knu.knudev.knuhubeducation.domain.TextAnswer;
import ua.knu.knudev.knuhubeducationapi.dto.TextAnswerPreviewDto;

@Mapper(componentModel = "spring")
public interface TextAnswerPreviewMapper extends BaseMapper<TextAnswer, TextAnswerPreviewDto> {
}
