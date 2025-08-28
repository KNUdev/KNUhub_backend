package ua.knu.knudev.knuhubeducation.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import ua.knu.knudev.fileserviceapi.api.ImageServiceApi;
import ua.knu.knudev.fileserviceapi.subfolder.ImageSubfolder;
import ua.knu.knudev.knuhubcommon.mapper.BaseMapper;
import ua.knu.knudev.knuhubeducation.domain.Image;
import ua.knu.knudev.knuhubeducation.domain.OptionQuestion;
import ua.knu.knudev.knuhubeducationapi.dto.OptionQuestionLiteDto;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring",
        uses = {
                OptionLiteMapper.class,
        })
public abstract class OptionQuestionLiteMapper implements BaseMapper<OptionQuestion, OptionQuestionLiteDto> {

    @Autowired
    ImageServiceApi imageServiceApi;

    @Mapping(target = "imagesPaths", source = "images")
    public abstract OptionQuestionLiteDto toDto(OptionQuestion optionQuestion);

    protected Set<String> mapImagesToImagesPaths(Set<Image> images) {
        return images.stream()
                .map(image -> imageServiceApi.getPathByFilename(image.getFilename(), ImageSubfolder.EDUCATION_TEST))
                .collect(Collectors.toSet());
    }
}
