package ua.knu.knudev.peoplemanagementapi.api;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import ua.knu.knudev.peoplemanagementapi.dto.educationalSpecialty.EducationalSpecialtyDto;
import ua.knu.knudev.peoplemanagementapi.request.educationalSpecialty.EducationalSpecialtyChangeRelationsRequest;
import ua.knu.knudev.peoplemanagementapi.request.educationalSpecialty.EducationalSpecialtyCreationRequest;
import ua.knu.knudev.peoplemanagementapi.request.educationalSpecialty.EducationalSpecialtyReceivingRequest;
import ua.knu.knudev.peoplemanagementapi.request.educationalSpecialty.EducationalSpecialtyUpdateRequest;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Validated
public interface EducationalSpecialtyApi {

    EducationalSpecialtyDto create(@Valid EducationalSpecialtyCreationRequest request);

    EducationalSpecialtyDto update(@Valid EducationalSpecialtyUpdateRequest request);

    void delete(String codeName);

    EducationalSpecialtyDto getByCodeName(String codeName);

    List<EducationalSpecialtyDto> getAll();

    Page<EducationalSpecialtyDto> getAll(EducationalSpecialtyReceivingRequest request);

    EducationalSpecialtyDto assignNewRelations(EducationalSpecialtyChangeRelationsRequest request);

    EducationalSpecialtyDto deleteRelations(EducationalSpecialtyChangeRelationsRequest request);
}
