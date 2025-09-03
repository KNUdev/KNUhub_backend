package ua.knu.knudev.knuhubeducation.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;
import ua.knu.knudev.fileserviceapi.api.ImageServiceApi;
import ua.knu.knudev.fileserviceapi.subfolder.ImageSubfolder;
import ua.knu.knudev.knuhubeducationapi.api.EducationImageServiceApi;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Validated
@Slf4j
public class EducationImageService implements EducationImageServiceApi {

    private final ImageServiceApi imageServiceApi;

    @Override
    public Set<String> uploadImagesWithCatchRemove(Set<MultipartFile> images) {
        if (images == null || images.isEmpty()) {
            return new HashSet<>();
        }

        Set<String> imageFilenames = new HashSet<>();

        try {
            for (MultipartFile image : images) {
                String filename = imageServiceApi.uploadFile(image, ImageSubfolder.EDUCATION_TEST);
                imageFilenames.add(filename);
            }
        } catch (Exception e) {
            removeImages(imageFilenames);

            throw e;
        }

        return imageFilenames;
    }

    @Override
    public void removeImages(Set<String> imageFilenames) {
        for (String filename : imageFilenames) {
            removeImage(filename);
        }
    }

    @Override
    public void removeImage(String filename) {
        try {
            imageServiceApi.removeByFilename(filename, ImageSubfolder.EDUCATION_TEST);
        } catch (Exception e) {
            log.warn(e.getMessage());
        }
    }

}
