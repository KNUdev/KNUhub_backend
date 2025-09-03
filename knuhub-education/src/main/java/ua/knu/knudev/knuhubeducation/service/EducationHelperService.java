package ua.knu.knudev.knuhubeducation.service;

import org.springframework.stereotype.Component;
import ua.knu.knudev.knuhubeducation.domain.Image;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class EducationHelperService {

    public static Set<String> getImagesFilenames(Set<Image> images) {
        return images.stream()
                .map(Image::getFilename)
                .collect(Collectors.toSet());
    }
}
