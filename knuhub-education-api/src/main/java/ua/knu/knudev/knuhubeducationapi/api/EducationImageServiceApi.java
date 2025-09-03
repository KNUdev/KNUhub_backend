package ua.knu.knudev.knuhubeducationapi.api;

import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

public interface EducationImageServiceApi {

    Set<String> uploadImagesWithCatchRemove(Set<MultipartFile> images);

    void removeImages(Set<String> imageFilenames);

    void removeImage(String filename);
}
