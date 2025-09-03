package ua.knu.knudev.knuhubeducation.mapper;

import org.mapstruct.Mapper;
import ua.knu.knudev.knuhubcommon.mapper.BaseMapper;
import ua.knu.knudev.knuhubeducation.domain.Option;
import ua.knu.knudev.knuhubeducationapi.dto.OptionLiteDto;

@Mapper(componentModel = "spring",
        uses = {
                ImageLiteMapper.class
        })
public interface OptionLiteMapper extends BaseMapper<Option, OptionLiteDto> {
}
