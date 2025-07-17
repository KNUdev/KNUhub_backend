package ua.knu.knudev.knuhubcommon.mapper;

import org.mapstruct.Mapper;
import ua.knu.knudev.knuhubcommon.domain.embeddable.FullName;
import ua.knu.knudev.knuhubcommon.dto.FullNameDto;

@Mapper(componentModel = "spring")
public interface FullNameMapper extends BaseMapper<FullName, FullNameDto> {
}
