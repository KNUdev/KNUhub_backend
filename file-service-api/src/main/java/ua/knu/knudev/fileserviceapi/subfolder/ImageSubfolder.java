package ua.knu.knudev.fileserviceapi.subfolder;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ImageSubfolder implements FileSubfolder {
    EDUCATION_TEST("/education/test");

    private final String subfolderPath;

    @Override
    public String getSubfolderPath() {
        return subfolderPath;
    }
}
