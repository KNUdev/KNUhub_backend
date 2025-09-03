package ua.knu.knudev.knuhubeducationapi.api;

import jakarta.validation.Valid;
import ua.knu.knudev.knuhubeducationapi.dto.OptionQuestionLiteDto;
import ua.knu.knudev.knuhubeducationapi.request.OptionQuestionCreationRequest;
import ua.knu.knudev.knuhubeducationapi.request.OptionQuestionUpdateRequest;

public interface OptionQuestionApi {

    OptionQuestionLiteDto create(@Valid OptionQuestionCreationRequest request);

    OptionQuestionLiteDto update(@Valid OptionQuestionUpdateRequest request);
}
