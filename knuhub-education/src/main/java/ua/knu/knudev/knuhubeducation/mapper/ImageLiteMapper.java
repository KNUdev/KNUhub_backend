package ua.knu.knudev.knuhubeducation.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
import ua.knu.knudev.fileserviceapi.api.ImageServiceApi;
import ua.knu.knudev.fileserviceapi.subfolder.ImageSubfolder;
import ua.knu.knudev.knuhubcommon.mapper.BaseMapper;
import ua.knu.knudev.knuhubeducation.domain.Image;
import ua.knu.knudev.knuhubeducationapi.dto.ImageLiteDto;

@Mapper(componentModel = "spring")
public abstract class ImageLiteMapper implements BaseMapper<Image, ImageLiteDto> {

    @Autowired
    ImageServiceApi imageServiceApi;

    @Mapping(target = "path", source = "filename", qualifiedByName = "mapFilenameToPath")
    public abstract ImageLiteDto toDto(Image image);

    @Named("mapFilenameToPath")
    protected String mapFilenameToPath(String filename) {
        return imageServiceApi.getPathByFilename(filename, ImageSubfolder.EDUCATION_TEST);
    }
}
