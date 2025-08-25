package ua.knu.knudev.knuhubeducation.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ua.knu.knudev.knuhubeducation.repository.OptionQuestionRepository;
import ua.knu.knudev.knuhubeducationapi.api.OptionQuestionApi;
import ua.knu.knudev.knuhubeducationapi.dto.OptionQuestionLiteDto;
import ua.knu.knudev.knuhubeducationapi.request.OptionQuestionCreationRequest;

@Service
@RequiredArgsConstructor
@Validated
@Slf4j
public class OptionQuestionService implements OptionQuestionApi {

//    @Autowired
//    private final OptionQuestionRepository repository;
//
//    public OptionQuestionLiteDto create(OptionQuestionCreationRequest request) {
//
//    }

}
