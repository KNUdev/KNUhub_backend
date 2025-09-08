package ua.knu.knudev.knuhubeducationapi.api;

import jakarta.validation.Valid;
import ua.knu.knudev.knuhubeducationapi.dto.MatchQuestionLiteDto;
import ua.knu.knudev.knuhubeducationapi.request.MatchQuestionCreationRequest;
import ua.knu.knudev.knuhubeducationapi.request.MatchQuestionUpdateRequest;

import java.util.UUID;

public interface MatchQuestionApi {

    MatchQuestionLiteDto create(@Valid MatchQuestionCreationRequest request);

    MatchQuestionLiteDto update(@Valid MatchQuestionUpdateRequest request);

    void delete(UUID id);

    MatchQuestionLiteDto findById(UUID id);
}
