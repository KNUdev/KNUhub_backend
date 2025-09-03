package ua.knu.knudev.knuhubeducationapi.api;

import jakarta.validation.Valid;
import ua.knu.knudev.knuhubeducationapi.dto.OptionQuestionLiteDto;
import ua.knu.knudev.knuhubeducationapi.request.OptionQuestionCreationRequest;
import ua.knu.knudev.knuhubeducationapi.request.OptionQuestionUpdateRequest;

import java.util.UUID;

public interface OptionQuestionApi {

    OptionQuestionLiteDto create(@Valid OptionQuestionCreationRequest request);

    OptionQuestionLiteDto update(@Valid OptionQuestionUpdateRequest request);

    void delete(UUID id);

    OptionQuestionLiteDto findById(UUID id);
}
