package ua.knu.knudev.peoplemanagementapi.api;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import ua.knu.knudev.peoplemanagementapi.dto.educationalGroup.EducationalGroupDto;
import ua.knu.knudev.peoplemanagementapi.request.educationalGroup.EducationGroupChangeRelationsRequest;
import ua.knu.knudev.peoplemanagementapi.request.educationalGroup.EducationalGroupCreationRequest;
import ua.knu.knudev.peoplemanagementapi.request.educationalGroup.EducationalGroupReceivingRequest;
import ua.knu.knudev.peoplemanagementapi.request.educationalGroup.EducationalGroupUpdateRequest;

import java.util.List;
import java.util.UUID;

@Validated
public interface EducationalGroupApi {

    EducationalGroupDto create(@Valid EducationalGroupCreationRequest request);

    EducationalGroupDto update(@Valid EducationalGroupUpdateRequest request);

    EducationalGroupDto getById(UUID id);

    void delete(UUID id);

    List<EducationalGroupDto> getAll();

    Page<EducationalGroupDto> getFilteredEducationalGroups(EducationalGroupReceivingRequest request);

    EducationalGroupDto assignNewRelations(@Valid EducationGroupChangeRelationsRequest request);

    EducationalGroupDto deleteRelations(@Valid EducationGroupChangeRelationsRequest request);

}
