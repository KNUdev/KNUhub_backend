package ua.knu.knudev.peoplemanagementapi.api;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import ua.knu.knudev.peoplemanagementapi.dto.faculty.FacultyDto;
import ua.knu.knudev.peoplemanagementapi.request.faculty.FacultyChangeRelationsRequest;
import ua.knu.knudev.peoplemanagementapi.request.faculty.FacultyReceivingRequest;
import ua.knu.knudev.peoplemanagementapi.request.faculty.FacultyCreationRequest;
import ua.knu.knudev.peoplemanagementapi.request.faculty.FacultyUpdateRequest;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Validated
public interface FacultyApi {

    FacultyDto create(@Valid FacultyCreationRequest request);

    FacultyDto update(@Valid FacultyUpdateRequest request);

    FacultyDto findById(UUID id);

    Page<FacultyDto> getFilteredAndPagedFaculties(FacultyReceivingRequest request);

    List<FacultyDto> getAllFaculties();

    void delete(@NotNull UUID facultyId);

    FacultyDto assignNewRelations(@Valid FacultyChangeRelationsRequest request);

    FacultyDto deleteRelations(@Valid FacultyChangeRelationsRequest request);

}