package ua.knu.knudev.peoplemanagement.service;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ua.knu.knudev.knuhubcommon.domain.embeddable.MultiLanguageField;
import ua.knu.knudev.knuhubcommon.mapper.MultiLanguageFieldMapper;
import ua.knu.knudev.peoplemanagement.domain.EducationalSpecialty;
import ua.knu.knudev.peoplemanagement.domain.Faculty;
import ua.knu.knudev.peoplemanagement.domain.User;
import ua.knu.knudev.peoplemanagement.mapper.FacultyMapper;
import ua.knu.knudev.peoplemanagement.repository.EducationalSpecialtyRepository;
import ua.knu.knudev.peoplemanagement.repository.FacultyRepository;
import ua.knu.knudev.peoplemanagement.repository.UserRepository;
import ua.knu.knudev.peoplemanagementapi.api.FacultyApi;
import ua.knu.knudev.peoplemanagementapi.dto.FacultyDto;
import ua.knu.knudev.peoplemanagementapi.request.FacultyReceivingRequest;
import ua.knu.knudev.peoplemanagementapi.exception.FacultyException;
import ua.knu.knudev.peoplemanagementapi.request.FacultyCreationRequest;
import ua.knu.knudev.peoplemanagementapi.request.FacultyUpdateRequest;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static ua.knu.knudev.peoplemanagement.service.HelperService.getOrDefault;

@Service
@RequiredArgsConstructor
@Validated
@Slf4j
public class FacultyService implements FacultyApi {

    private final FacultyRepository facultyRepository;
    private final EducationalSpecialtyRepository educationalSpecialtyRepository;
    private final UserRepository userRepository;
    private final MultiLanguageFieldMapper multiLanguageFieldMapper;
    private final FacultyMapper facultyMapper;

    @Override
    @Transactional
    public FacultyDto create(@Valid FacultyCreationRequest request) {
        validateFacultyNames(request.facultyName().getEn(), request.facultyName().getUk());

        List<EducationalSpecialty> educationalSpecialties = request.educationalSpecialtyIds() != null
                ? educationalSpecialtyRepository.findAllById(request.educationalSpecialtyIds())
                : Collections.emptyList();
        List<User> users = request.userIds() != null
                ? userRepository.findAllById(request.userIds())
                : Collections.emptyList();

        Faculty faculty = Faculty.builder()
                .name(multiLanguageFieldMapper.toDomain(request.facultyName()))
                .educationalSpecialties(new HashSet<>(educationalSpecialties))
                .users(new HashSet<>(users))
                .build();

        Faculty savedFaculty = facultyRepository.save(faculty);

        log.info("Created faculty with id {}", savedFaculty.getId());
        return facultyMapper.toDto(savedFaculty);
    }

    @Override
    public FacultyDto update(@Valid FacultyUpdateRequest request) {
        Faculty faculty = getFacultyById(request.facultyId());

        String facultyEnName = getOrDefault(request.newFacultyEnName(), faculty.getName().getEn());
        String facultyUkName = getOrDefault(request.newFacultyUkName(), faculty.getName().getUk());

        validateFacultyNames(facultyEnName, facultyUkName);

        faculty.setName(MultiLanguageField.builder()
                .en(facultyEnName)
                .uk(facultyUkName)
                .build());

        Faculty savedFaculty = facultyRepository.save(faculty);

        log.info("Updated faculty with id {}", savedFaculty.getId());
        return facultyMapper.toDto(savedFaculty);
    }

    @Override
    @Transactional
    public void delete(UUID facultyId) {
        facultyRepository.deleteById(facultyId);
        log.info("Deleted faculty with id {}", facultyId);
    }

    @Override
    @Transactional
    public FacultyDto findById(UUID id) {
        Faculty faculty = getFacultyById(id);
        log.info("Found faculty with id {}", faculty.getId());
        return facultyMapper.toDto(faculty);
    }

    @Override
    @Transactional
    public Page<FacultyDto> getFilteredAndPagedFaculties(FacultyReceivingRequest request) {
        int pageNumber = getOrDefault(request.pageNumber(), 0);
        int pageSize = getOrDefault(request.pageSize(), 10);

        PageRequest paging = PageRequest.of(pageNumber, pageSize);
        Page<Faculty> facultiesPage = facultyRepository.findAllBySearchQuery(request, paging);

        return facultiesPage.map(facultyMapper::toDto);
    }

    @Override
    @Transactional
    public List<FacultyDto> getAllFaculties() {
        return facultyMapper.toDtos(facultyRepository.findAll());
    }

    @Override
    @Transactional
    public FacultyDto assignNewEducationalSpecialties(UUID facultyId, Set<String> educationalSpecialtyIds) {
        Faculty faculty = getFacultyById(facultyId);

        Set<EducationalSpecialty> educationalSpecialties = extractEducationalSpecialtiesFromIds(educationalSpecialtyIds);
        faculty.addEducationalSpecialties(educationalSpecialties);

        Faculty savedFaculty = facultyRepository.save(faculty);
        savedFaculty.associateEducationalSpecialtiesAndUsersWithFaculty();
        List<String> addedEducationSpecialtiesIds = educationalSpecialties.stream()
                .map(EducationalSpecialty::getCodeName).toList();

        log.info("Assigned educational specialties with ids: {}, to the faculty with id: {}",
                addedEducationSpecialtiesIds, facultyId);
        return facultyMapper.toDto(savedFaculty);
    }

    @Override
    @Transactional
    public FacultyDto deleteEducationalSpecialties(UUID facultyId, Set<String> educationalSpecialtyIds) {
        Faculty faculty = getFacultyById(facultyId);

        Set<EducationalSpecialty> educationalSpecialties = extractEducationalSpecialtiesFromIds(educationalSpecialtyIds);
        faculty.deleteEducationalSpecialties(educationalSpecialties);

        Faculty savedFaculty = facultyRepository.save(faculty);
        savedFaculty.associateEducationalSpecialtiesAndUsersWithFaculty();
        List<String> deletedEducationalSpecialtiesIds = educationalSpecialties.stream()
                .map(EducationalSpecialty::getCodeName).toList();

        log.info("Deleted educational specialties with ids: {}, from the faculty with id: {}",
                deletedEducationalSpecialtiesIds, facultyId);
        return facultyMapper.toDto(savedFaculty);
    }

    @Override
    @Transactional
    public FacultyDto assignNewUsers(UUID facultyId, Set<UUID> userIds) {
        Faculty faculty = getFacultyById(facultyId);

        Set<User> users = extractUsersFromIds(userIds);
        faculty.addUsers(users);

        Faculty savedFaculty = facultyRepository.save(faculty);
        savedFaculty.associateEducationalSpecialtiesAndUsersWithFaculty();
        List<UUID> addedUserIds = users.stream().map(User::getId).toList();

        log.info("Assigned users with ids: {}, to the faculty with id: {}", addedUserIds, facultyId);
        return facultyMapper.toDto(savedFaculty);
    }

    @Override
    @Transactional
    public FacultyDto deleteUsers(UUID facultyId, Set<UUID> userIds) {
        Faculty faculty = getFacultyById(facultyId);

        Set<User> users = extractUsersFromIds(userIds);
        faculty.deleteUsers(users);

        Faculty savedFaculty = facultyRepository.save(faculty);
        savedFaculty.associateEducationalSpecialtiesAndUsersWithFaculty();
        List<UUID> deletedUserIds = users.stream().map(User::getId).toList();

        log.info("Deleted users with ids: {}, from the faculty with id: {}", deletedUserIds, facultyId);
        return facultyMapper.toDto(savedFaculty);
    }

    private void validateFacultyNames(String facultyEnName, String facultyUkName) {
        if (facultyEnName != null && !facultyEnName.matches("^[a-zA-Z0-9\\s.,;:'\"\\-()]+$")) {
            throw new FacultyException("Faculty English name must contain only English letters!");
        }

        if (facultyUkName != null && !facultyUkName.matches("^[а-яА-ЯіІїЇєЄґҐ0-9\\s.,;:'\"\\-()]+$")) {
            throw new FacultyException("Faculty Ukrainian name must contain only Ukrainian letters!");
        }
    }

    private Set<EducationalSpecialty> extractEducationalSpecialtiesFromIds(Set<String> educationalSpecialtyIds) {
        List<EducationalSpecialty> educationalSpecialties = educationalSpecialtyRepository.findAllById(educationalSpecialtyIds);

        if (educationalSpecialtyIds.size() != educationalSpecialties.size()) {
            List<String> notFoundIds = decideWhichIdsWereNotFound(
                    educationalSpecialtyIds,
                    educationalSpecialties,
                    EducationalSpecialty::getCodeName
            );
            log.error("Educational specialties with ids: {} not found", notFoundIds);
        }

        return new HashSet<>(educationalSpecialties);
    }

    private Set<User> extractUsersFromIds(Set<UUID> userIds) {
        List<User> users = userRepository.findAllById(userIds);

        if (userIds.size() != users.size()) {
            List<UUID> notFoundIds = decideWhichIdsWereNotFound(
                    userIds,
                    users,
                    User::getId
            );
            log.error("Users with ids: {} not found", notFoundIds);
        }

        return new HashSet<>(users);
    }

    private <T, U> List<T> decideWhichIdsWereNotFound(Set<T> receivedIds, Collection<U> entities, Function<U, T> idExtractor) {
        if (receivedIds == null || entities == null || idExtractor == null) {
            throw new IllegalArgumentException("Arguments cannot be null");
        }

        Set<T> foundIds = entities.stream()
                .map(idExtractor)
                .collect(Collectors.toSet());

        return receivedIds.stream()
                .filter(id -> !foundIds.contains(id))
                .collect(Collectors.toList());
    }

    private Faculty getFacultyById(UUID facultyId) {
        return facultyRepository.findById(facultyId).orElseThrow(() ->
                new FacultyException("Faculty with id " + facultyId + " not found!"));
    }

}
