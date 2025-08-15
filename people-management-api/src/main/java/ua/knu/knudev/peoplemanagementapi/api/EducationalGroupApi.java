package ua.knu.knudev.peoplemanagementapi.api;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import ua.knu.knudev.peoplemanagementapi.dto.educationalGroup.EducationalGroupDto;
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

    EducationalGroupDto assignNewStudents(@NotNull UUID educationalGroupId, @NotEmpty List<UUID> studentIds);

    EducationalGroupDto deleteStudents(@NotNull UUID educationalGroupId, @NotEmpty List<UUID> studentIds);

    EducationalGroupDto assignNewTeachers(@NotNull UUID educationalGroupId, @NotEmpty List<UUID> teacherIds);

    EducationalGroupDto deleteTeachers(@NotNull UUID educationalGroupId, @NotEmpty List<UUID> teacherIds);

    EducationalGroupDto assignNewEducationalSpecialties(@NotNull UUID educationalGroupId,
                                                        @NotEmpty List<String> educationalSpecialtyCodeNames);

    EducationalGroupDto deleteEducationalSpecialties(@NotNull UUID educationalGroupId,
                                                     @NotEmpty List<String> educationalSpecialtyCodeNames);

    EducationalGroupDto assignNewSubjects(@NotNull UUID educationalGroupId, @NotEmpty List<UUID> subjectIds);

    EducationalGroupDto deleteSubjects(@NotNull UUID educationalGroupId, @NotEmpty List<UUID> subjectIds);

}
