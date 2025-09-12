package ua.knu.knudev.knuhubeducationapi.api;

import jakarta.validation.Valid;
import ua.knu.knudev.knuhubeducationapi.dto.TextQuestionLiteDto;
import ua.knu.knudev.knuhubeducationapi.request.TextQuestionCreationRequest;
import ua.knu.knudev.knuhubeducationapi.request.TextQuestionUpdateRequest;

import java.util.UUID;

public interface TextQuestionApi {

    TextQuestionLiteDto create(@Valid TextQuestionCreationRequest request);

    TextQuestionLiteDto update(@Valid TextQuestionUpdateRequest request);

    void delete(UUID id);

    TextQuestionLiteDto findById(UUID id);
}
