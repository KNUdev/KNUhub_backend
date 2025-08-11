package ua.knu.knudev.peoplemanagementapi.api;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import ua.knu.knudev.peoplemanagementapi.dto.EducationalSpecialtyDto;
import ua.knu.knudev.peoplemanagementapi.request.EducationalSpecialtyCreationRequest;
import ua.knu.knudev.peoplemanagementapi.request.EducationalSpecialtyReceivingRequest;
import ua.knu.knudev.peoplemanagementapi.request.EducationalSpecialtyUpdateRequest;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Validated
public interface EducationalSpecialtyApi {

    EducationalSpecialtyDto create(EducationalSpecialtyCreationRequest request);

    EducationalSpecialtyDto update(EducationalSpecialtyUpdateRequest request);

    void delete(String codeName);

    EducationalSpecialtyDto getByCodeName(String codeName);

    List<EducationalSpecialtyDto> getAll();

    Page<EducationalSpecialtyDto> getAll(EducationalSpecialtyReceivingRequest request);

    EducationalSpecialtyDto assignNewFaculties(@NotNull String educationalSpecialtyCodeName,
                                               @NotEmpty Set<@NotNull UUID> facultiesIds);

    EducationalSpecialtyDto deleteFaculties(@NotNull String educationalSpecialtyCodeName,
                                            @NotEmpty Set<@NotNull UUID> facultiesIds);

    EducationalSpecialtyDto assignNewGroups(@NotNull String educationalSpecialtyCodeName,
                                            @NotEmpty Set<@NotNull UUID> groupsIds);

    EducationalSpecialtyDto deleteGroups(@NotNull String educationalSpecialtyCodeName,
                                         @NotEmpty Set<@NotNull UUID> groupsIds);

    EducationalSpecialtyDto assignNewStudents(@NotNull String educationalSpecialtyCodeName,
                                              @NotEmpty Set<@NotNull UUID> studentsIds);

    EducationalSpecialtyDto deleteStudents(@NotNull String educationalSpecialtyCodeName,
                                           @NotEmpty Set<@NotNull UUID> studentsIds);

    EducationalSpecialtyDto assignNewTeachers(@NotNull String educationalSpecialtyCodeName,
                                              @NotEmpty Set<@NotNull UUID> teachersIds);

    EducationalSpecialtyDto deleteTeachers(@NotNull String educationalSpecialtyCodeName,
                                           @NotEmpty Set<@NotNull UUID> teachersIds);

    EducationalSpecialtyDto assignNewTeachingAssigments(@NotNull String educationalSpecialtyCodeName,
                                                        @NotEmpty Set<@NotNull UUID> teachingAssigmentsIds);

    EducationalSpecialtyDto deleteTeachingAssigments(@NotNull String educationalSpecialtyCodeName,
                                                     @NotEmpty Set<@NotNull UUID> teachingAssigmentsIds);
}
