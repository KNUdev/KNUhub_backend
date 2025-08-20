package ua.knu.knudev.knuhubeducationapi.api;

import jakarta.validation.Valid;
import ua.knu.knudev.knuhubeducationapi.dto.TestDto;
import ua.knu.knudev.knuhubeducationapi.dto.TestPreviewDto;
import ua.knu.knudev.knuhubeducationapi.request.TestCreationRequest;
import ua.knu.knudev.knuhubeducationapi.request.TestUpdateRequest;

import java.util.UUID;

public interface TestApi {

    TestDto createTest(@Valid TestCreationRequest request);

    TestDto updateTest(@Valid TestUpdateRequest request);

    void delete(UUID testId);

    TestDto findById(UUID testId);

    TestPreviewDto findTestPreviewById(UUID testId);

}
