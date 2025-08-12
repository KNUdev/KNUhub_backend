package ua.knu.knudev.knuhubeducationapi.api;

import jakarta.validation.Valid;
import ua.knu.knudev.knuhubeducationapi.dto.TestDto;
import ua.knu.knudev.knuhubeducationapi.request.TestCreationRequest;
import ua.knu.knudev.knuhubeducationapi.request.TestUpdateRequest;

public interface TestApi {

    TestDto createTest(@Valid TestCreationRequest request);

    TestDto updateTest(@Valid TestUpdateRequest request);

}
