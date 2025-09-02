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
import ua.knu.knudev.peoplemanagement.mapper.faculty.FacultyMapper;
import ua.knu.knudev.peoplemanagement.repository.EducationalSpecialtyRepository;
import ua.knu.knudev.peoplemanagement.repository.FacultyRepository;
import ua.knu.knudev.peoplemanagement.repository.UserRepository;
import ua.knu.knudev.peoplemanagementapi.api.FacultyApi;
import ua.knu.knudev.peoplemanagementapi.dto.faculty.FacultyDto;
import ua.knu.knudev.peoplemanagementapi.exception.FacultyException;
import ua.knu.knudev.peoplemanagementapi.request.faculty.FacultyChangeRelationsRequest;
import ua.knu.knudev.peoplemanagementapi.request.faculty.FacultyCreationRequest;
import ua.knu.knudev.peoplemanagementapi.request.faculty.FacultyReceivingRequest;
import ua.knu.knudev.peoplemanagementapi.request.faculty.FacultyUpdateRequest;

import java.util.HashSet;
import java.util.List;
import java.util.UUID;

import static ua.knu.knudev.knuhubcommon.service.HelperService.*;

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
        validateMultiLanguageFields(
                request.facultyName().getEn(),
                request.facultyName().getUk(),
                name -> new FacultyException("Invalid faculty names: " + name)
        );

        List<EducationalSpecialty> educationalSpecialties = extractEntities(
                request.educationalSpecialtyIds(),
                educationalSpecialtyRepository
        );

        List<User> users = extractEntities(request.userIds(), userRepository);

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
        Faculty faculty = extractFacultyFromId(request.facultyId());

        String facultyEnName = getOrDefault(request.newFacultyEnName(), faculty.getName().getEn());
        String facultyUkName = getOrDefault(request.newFacultyUkName(), faculty.getName().getUk());

        validateMultiLanguageFields(
                facultyEnName,
                facultyUkName,
                name -> new FacultyException("Invalid faculty names: " + name)
        );

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
        Faculty faculty = extractFacultyFromId(id);

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
    public FacultyDto assignNewRelations(FacultyChangeRelationsRequest request) {
        Faculty faculty = extractFacultyFromId(request.facultyId());

        if (request.educationalSpecialtyIds() != null) {
            List<EducationalSpecialty> educationalSpecialties = extractEntities(
                    request.educationalSpecialtyIds(),
                    educationalSpecialtyRepository
            );
            faculty.addEducationalSpecialties(educationalSpecialties);
        }
        if (request.userIds() != null) {
            List<User> users = extractEntities(request.userIds(), userRepository);
            faculty.addUsers(users);
        }

        faculty.associateEducationalSpecialtiesAndUsersWithFaculty();
        Faculty savedFaculty = facultyRepository.save(faculty);

        log.info("Assigned new relations to faculty with id {}", savedFaculty.getId());
        return facultyMapper.toDto(savedFaculty);
    }

    @Override
    @Transactional
    public FacultyDto deleteRelations(FacultyChangeRelationsRequest request) {
        Faculty faculty = extractFacultyFromId(request.facultyId());

        if (request.educationalSpecialtyIds() != null) {
            List<EducationalSpecialty> educationalSpecialties = extractEntities(
                    request.educationalSpecialtyIds(),
                    educationalSpecialtyRepository
            );
            faculty.deleteEducationalSpecialties(educationalSpecialties);
        }
        if (request.userIds() != null) {
            List<User> users = extractEntities(request.userIds(), userRepository);
            faculty.deleteUsers(users);
        }

        faculty.associateEducationalSpecialtiesAndUsersWithFaculty();
        Faculty savedFaculty = facultyRepository.save(faculty);

        log.info("Deleted relations from faculty with id {}", savedFaculty.getId());
        FacultyDto dto = facultyMapper.toDto(savedFaculty);
        return dto;
    }

    private Faculty extractFacultyFromId(UUID facultyId) {
        return extractEntity(
                facultyId,
                facultyRepository,
                id -> new FacultyException("Faculty with id " + id + " not found")
        );
    }

}
