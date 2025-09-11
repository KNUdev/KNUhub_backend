package ua.knu.knudev.knuhubeducation.mapper;

import org.mapstruct.Mapper;
import ua.knu.knudev.knuhubcommon.mapper.BaseMapper;
import ua.knu.knudev.knuhubeducation.domain.matching.MatchingPair;
import ua.knu.knudev.knuhubeducationapi.dto.MatchingPairLiteDto;

@Mapper(componentModel = "spring")
public interface MatchPairLiteMapper extends BaseMapper<MatchingPair, MatchingPairLiteDto> {
}
