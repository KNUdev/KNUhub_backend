package ua.knu.knudev.knuhubcommon.mapper;

import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Named;
import ua.knu.knudev.knuhubcommon.domain.embeddable.MultiLanguageField;
import ua.knu.knudev.knuhubcommon.dto.MultiLanguageFieldDto;

@Mapper(componentModel = "spring")
public interface MultiLanguageFieldMapper extends BaseMapper<MultiLanguageField, MultiLanguageFieldDto> {

    @Named("toDtoWithContext")
    @Override
    MultiLanguageFieldDto toDto(MultiLanguageField multiLanguageField, @Context CycleAvoidingMappingContext context);

    @Named("toDomainWithContext")
    @Override
    MultiLanguageField toDomain(MultiLanguageFieldDto dto, @Context CycleAvoidingMappingContext context);
}