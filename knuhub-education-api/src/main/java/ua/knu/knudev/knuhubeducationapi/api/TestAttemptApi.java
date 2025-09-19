package ua.knu.knudev.knuhubeducationapi.api;

import jakarta.validation.Valid;
import ua.knu.knudev.knuhubeducationapi.dto.OptionAnswerPreviewDto;
import ua.knu.knudev.knuhubeducationapi.dto.TestAttemptDto;
import ua.knu.knudev.knuhubeducationapi.dto.TextAnswerPreviewDto;
import ua.knu.knudev.knuhubeducationapi.dto.match.MatchAnswerPreviewDto;
import ua.knu.knudev.knuhubeducationapi.request.MatchAnswerSaveRequest;
import ua.knu.knudev.knuhubeducationapi.request.OptionAnswerSaveRequest;
import ua.knu.knudev.knuhubeducationapi.request.TestAttemptCreationRequest;
import ua.knu.knudev.knuhubeducationapi.request.TextAnswerSaveRequest;

import java.util.UUID;

public interface TestAttemptApi {

    TestAttemptDto createTestAttempt(@Valid TestAttemptCreationRequest request);

    void deleteTestAttempt(UUID id);

    OptionAnswerPreviewDto saveOptionAnswer(OptionAnswerSaveRequest request);

    TextAnswerPreviewDto saveTextAnswer(TextAnswerSaveRequest request);

    MatchAnswerPreviewDto saveMatchAnswer(MatchAnswerSaveRequest request);
}
