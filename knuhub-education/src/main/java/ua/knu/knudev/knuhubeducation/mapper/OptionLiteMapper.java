package ua.knu.knudev.knuhubeducation.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import ua.knu.knudev.fileserviceapi.api.ImageServiceApi;
import ua.knu.knudev.fileserviceapi.subfolder.ImageSubfolder;
import ua.knu.knudev.knuhubcommon.mapper.BaseMapper;
import ua.knu.knudev.knuhubeducation.domain.Image;
import ua.knu.knudev.knuhubeducation.domain.Option;
import ua.knu.knudev.knuhubeducationapi.dto.OptionLiteDto;

@Mapper(componentModel = "spring")
public abstract class OptionLiteMapper implements BaseMapper<Option, OptionLiteDto> {

    @Autowired
    ImageServiceApi imageServiceApi;

    @Mapping(target = "imagePath", source = "image")
    public abstract OptionLiteDto toDto(Option option);

    protected String mapImageToImagePath(Image image) {
        return imageServiceApi.getPathByFilename(image.getFilename(), ImageSubfolder.EDUCATION_TEST);
    }
}
