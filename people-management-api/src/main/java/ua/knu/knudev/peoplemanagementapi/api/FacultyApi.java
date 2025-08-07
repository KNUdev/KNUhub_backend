package ua.knu.knudev.peoplemanagementapi.api;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;
import ua.knu.knudev.peoplemanagementapi.dto.FacultyDto;
import ua.knu.knudev.peoplemanagementapi.request.FacultyCreationRequest;
import ua.knu.knudev.peoplemanagementapi.request.FacultyUpdateRequest;

import java.util.Set;
import java.util.UUID;

@Validated
public interface FacultyApi {

    FacultyDto create(@Valid FacultyCreationRequest request);

    FacultyDto update(@Valid FacultyUpdateRequest request);

    void delete(@NotNull UUID facultyId);

    FacultyDto assignNewEducationalGroups(@NotNull UUID facultyId,
                                          @NotEmpty Set<@NotNull UUID> educationalGroupIds);

    FacultyDto deleteEducationalGroups(@NotNull UUID facultyId,
                                       @NotEmpty Set<@NotNull UUID> educationalGroupIds);

    FacultyDto assignNewEducationalSpecialties(@NotNull UUID facultyId,
                                               @NotEmpty Set<@NotNull String> educationalSpecialtyIds);

    FacultyDto deleteEducationalSpecialties(@NotNull UUID facultyId,
                                            @NotEmpty Set<@NotNull String> educationalSpecialtyIds);

    FacultyDto assignNewUsers(@NotNull UUID facultyId,
                              @NotEmpty Set<@NotNull UUID> userIds);

    FacultyDto deleteUsers(@NotNull UUID facultyId,
                           @NotEmpty Set<@NotNull UUID> userIds);

}