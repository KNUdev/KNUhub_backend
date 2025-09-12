package ua.knu.knudev.knuhubeducation.mapper;

import org.mapstruct.Mapper;
import ua.knu.knudev.knuhubcommon.mapper.BaseMapper;
import ua.knu.knudev.knuhubeducation.domain.TextQuestion;
import ua.knu.knudev.knuhubeducationapi.dto.TextQuestionLiteDto;

@Mapper(componentModel = "spring")
public interface TextQuestionLiteMapper extends BaseMapper<TextQuestion, TextQuestionLiteDto> {
}
