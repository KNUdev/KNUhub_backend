package ua.knu.knudev.knuhubeducation.mapper;

import org.mapstruct.Mapper;
import ua.knu.knudev.knuhubcommon.mapper.BaseMapper;
import ua.knu.knudev.knuhubeducation.domain.OptionAnswer;
import ua.knu.knudev.knuhubeducationapi.dto.OptionAnswerPreviewDto;

@Mapper(componentModel = "spring")
public interface OptionAnswerPreviewMapper extends BaseMapper<OptionAnswer, OptionAnswerPreviewDto> {
}
