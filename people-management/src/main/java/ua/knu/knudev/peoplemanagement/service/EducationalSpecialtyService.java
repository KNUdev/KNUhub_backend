package ua.knu.knudev.peoplemanagement.service;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ua.knu.knudev.knuhubcommon.domain.embeddable.MultiLanguageField;
import ua.knu.knudev.knuhubcommon.mapper.MultiLanguageFieldMapper;
import ua.knu.knudev.peoplemanagement.domain.*;
import ua.knu.knudev.peoplemanagement.mapper.EducationalSpecialtyMapper;
import ua.knu.knudev.peoplemanagement.repository.*;
import ua.knu.knudev.peoplemanagementapi.api.EducationalSpecialtyApi;
import ua.knu.knudev.peoplemanagementapi.dto.EducationalSpecialtyDto;
import ua.knu.knudev.peoplemanagementapi.exception.EducationalSpecialtyException;
import ua.knu.knudev.peoplemanagementapi.request.EducationalSpecialtyCreationRequest;
import ua.knu.knudev.peoplemanagementapi.request.EducationalSpecialtyReceivingRequest;
import ua.knu.knudev.peoplemanagementapi.request.EducationalSpecialtyUpdateRequest;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static ua.knu.knudev.knuhubcommon.service.HelperService.*;

@RequiredArgsConstructor
@Service
@Slf4j
@Validated
public class EducationalSpecialtyService implements EducationalSpecialtyApi {

    private final MultiLanguageFieldMapper multiLanguageFieldMapper;
    private final EducationalSpecialtyMapper educationalSpecialtyMapper;
    private final EducationalSpecialtyRepository educationalSpecialtyRepository;
    private final FacultyRepository facultyRepository;
    private final EducationalGroupRepository educationalGroupRepository;
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
    private final TeachingAssigmentRepository teachingAssigmentRepository;

    @Override
    @Transactional
    public EducationalSpecialtyDto create(@Valid EducationalSpecialtyCreationRequest request) {
        checkIfEducationalSpecialtyAlreadyExists(request);
        validateMultiLanguageFields(request.name().getEn(), request.name().getUk(),
                name -> new EducationalSpecialtyException("Invalid multi-language field: " + name));

        List<Faculty> faculties = extractEntitiesFromIds(request.facultyIds(), facultyRepository);
        List<Student> students = extractEntitiesFromIds(request.studentIds(), studentRepository);
        List<Teacher> teachers = extractEntitiesFromIds(request.teacherIds(), teacherRepository);
        List<EducationalGroup> educationalGroups = extractEntitiesFromIds(request.educationalGroupIds(), educationalGroupRepository);
        List<TeachingAssigment> teachingAssigments = extractEntitiesFromIds(request.teachingAssigmentIds(), teachingAssigmentRepository);

        EducationalSpecialty educationalSpecialty = EducationalSpecialty.builder()
                .codeName(request.codeName())
                .name(multiLanguageFieldMapper.toDomain(request.name()))
                .faculties(new HashSet<>(faculties))
                .students(new HashSet<>(students))
                .teachers(new HashSet<>(teachers))
                .groups(new HashSet<>(educationalGroups))
                .teachingAssigments(new HashSet<>(teachingAssigments))
                .build();

        educationalSpecialty.associateAllInjectedEntitiesWithEducationalSpecialty();
        EducationalSpecialty savedEducationalSpecialty = educationalSpecialtyRepository.save(educationalSpecialty);
        log.info("Created new educational specialty with code name: {}", savedEducationalSpecialty.getCodeName());

        return educationalSpecialtyMapper.toDto(savedEducationalSpecialty);
    }

    @Override
    public EducationalSpecialtyDto update(@Valid EducationalSpecialtyUpdateRequest request) {
        EducationalSpecialty educationalSpecialty = extractEntityById(request.codeName(), educationalSpecialtyRepository,
                id -> new EducationalSpecialtyException("Educational specialty with code name: " + id + " not found."));

        validateMultiLanguageFields(request.enSpecialtyName(), request.ukSpecialtyName(),
                name -> new EducationalSpecialtyException("Invalid multi-language field: " + name));

        String specialtyEnName = getOrDefault(request.enSpecialtyName(), educationalSpecialty.getName().getEn());
        String specialtyUkName = getOrDefault(request.ukSpecialtyName(), educationalSpecialty.getName().getUk());

        MultiLanguageField newSpecialtyName = MultiLanguageField.builder()
                .en(specialtyEnName)
                .uk(specialtyUkName)
                .build();

        educationalSpecialty.setName(newSpecialtyName);
        EducationalSpecialty savedEducationalSpecialty = educationalSpecialtyRepository.save(educationalSpecialty);
        log.info("Updated educational specialty with code name: {}", educationalSpecialty.getCodeName());

        return educationalSpecialtyMapper.toDto(savedEducationalSpecialty);
    }

    @Override
    public void delete(String codeName) {
        educationalSpecialtyRepository.deleteById(codeName);
        log.info("Deleted educational specialty with code name: {}", codeName);
    }

    @Override
    @Transactional
    public EducationalSpecialtyDto getByCodeName(String codeName) {
        EducationalSpecialty educationalSpecialty = extractEntityById(codeName, educationalSpecialtyRepository,
                id -> new EducationalSpecialtyException("Educational specialty with code name: " + id + " not found."));
        log.info("Retrieved educational specialty with code name: {}", codeName);
        return educationalSpecialtyMapper.toDto(educationalSpecialty);
    }

    @Override
    public List<EducationalSpecialtyDto> getAll() {
        List<EducationalSpecialty> educationalSpecialties = educationalSpecialtyRepository.findAll();
        log.info("Retrieved all educational specialties, count: {}", educationalSpecialties.size());
        return educationalSpecialtyMapper.toDtos(educationalSpecialties);
    }

    @Override
    public Page<EducationalSpecialtyDto> getAll(EducationalSpecialtyReceivingRequest request) {
        int pageNumber = getOrDefault(request.pageNumber(), 0);
        int pageSize = getOrDefault(request.pageSize(), 10);

        PageRequest paging = PageRequest.of(pageNumber, pageSize);

        Page<EducationalSpecialty> educationalSpecialties = educationalSpecialtyRepository
                .findAllBySearchQuery(request, paging);

        log.info("Retrieved educational specialties by search query: {}, count: {}", request.searchQuery(),
                educationalSpecialties.getTotalElements());

        return educationalSpecialties.map(educationalSpecialtyMapper::toDto);
    }

    @Override
    @Transactional
    public EducationalSpecialtyDto assignNewFaculties(
            @NotNull String educationalSpecialtyCodeName,
            @NotEmpty Set<UUID> facultiesIds
    ) {
        EducationalSpecialty educationalSpecialty = extractEntityById(
                educationalSpecialtyCodeName,
                educationalSpecialtyRepository,
                codeName -> new EducationalSpecialtyException("Educational specialty with code name: " + codeName + " not found.")
        );

        List<Faculty> faculties = extractEntitiesFromIds(facultiesIds, facultyRepository);
        educationalSpecialty.addFaculties(new HashSet<>(faculties));
        educationalSpecialty.associateAllInjectedEntitiesWithEducationalSpecialty();
        EducationalSpecialty savedEducationalSpecialty = educationalSpecialtyRepository.save(educationalSpecialty);

        log.info("Assigned new faculties to educational specialty with code name: {}", educationalSpecialtyCodeName);

        return educationalSpecialtyMapper.toDto(savedEducationalSpecialty);
    }

    @Override
    public EducationalSpecialtyDto deleteFaculties(
            @NotNull String educationalSpecialtyCodeName,
            @NotEmpty Set<UUID> facultiesIds
    ) {
        EducationalSpecialty educationalSpecialty = extractEntityById(
                educationalSpecialtyCodeName,
                educationalSpecialtyRepository,
                codeName -> new EducationalSpecialtyException("Educational specialty with code name: " + codeName + " not found.")
        );

        List<Faculty> faculties = extractEntitiesFromIds(facultiesIds, facultyRepository);
        educationalSpecialty.deleteFaculties(new HashSet<>(faculties));
        educationalSpecialty.associateAllInjectedEntitiesWithEducationalSpecialty();
        EducationalSpecialty savedEducationalSpecialty = educationalSpecialtyRepository.save(educationalSpecialty);

        log.info("Deleted faculties from educational specialty with code name: {}", educationalSpecialtyCodeName);

        return educationalSpecialtyMapper.toDto(savedEducationalSpecialty);
    }

    @Override
    @Transactional
    public EducationalSpecialtyDto assignNewGroups(
            @NotNull String educationalSpecialtyCodeName,
            @NotEmpty Set<UUID> groupsIds
    ) {
        EducationalSpecialty educationalSpecialty = extractEntityById(
                educationalSpecialtyCodeName,
                educationalSpecialtyRepository,
                codeName -> new EducationalSpecialtyException("Educational specialty with code name: " + codeName + " not found.")
        );

        List<EducationalGroup> educationalGroups = extractEntitiesFromIds(groupsIds, educationalGroupRepository);
        educationalSpecialty.addGroups(new HashSet<>(educationalGroups));
        educationalSpecialty.associateAllInjectedEntitiesWithEducationalSpecialty();
        EducationalSpecialty savedEducationalSpecialty = educationalSpecialtyRepository.save(educationalSpecialty);

        log.info("Assigned new groups to educational specialty with code name: {}", educationalSpecialtyCodeName);

        return educationalSpecialtyMapper.toDto(savedEducationalSpecialty);
    }

    @Override
    @Transactional
    public EducationalSpecialtyDto deleteGroups(
            @NotNull String educationalSpecialtyCodeName,
            @NotEmpty Set<UUID> groupsIds
    ) {
        EducationalSpecialty educationalSpecialty = extractEntityById(
                educationalSpecialtyCodeName,
                educationalSpecialtyRepository,
                codeName -> new EducationalSpecialtyException("Educational specialty with code name: " + codeName + " not found.")
        );

        List<EducationalGroup> educationalGroups = extractEntitiesFromIds(groupsIds, educationalGroupRepository);
        educationalSpecialty.deleteGroups(new HashSet<>(educationalGroups));
        educationalSpecialty.associateAllInjectedEntitiesWithEducationalSpecialty();
        EducationalSpecialty savedEducationalSpecialty = educationalSpecialtyRepository.save(educationalSpecialty);

        log.info("Deleted groups from educational specialty with code name: {}", educationalSpecialtyCodeName);

        return educationalSpecialtyMapper.toDto(savedEducationalSpecialty);
    }

    @Override
    @Transactional
    public EducationalSpecialtyDto assignNewStudents(
            @NotNull String educationalSpecialtyCodeName,
            @NotEmpty Set<UUID> studentsIds
    ) {
        EducationalSpecialty educationalSpecialty = extractEntityById(
                educationalSpecialtyCodeName,
                educationalSpecialtyRepository,
                codeName -> new EducationalSpecialtyException("Educational specialty with code name: " + codeName + " not found.")
        );

        List<Student> students = extractEntitiesFromIds(studentsIds, studentRepository);
        educationalSpecialty.addStudents(new HashSet<>(students));
        educationalSpecialty.associateAllInjectedEntitiesWithEducationalSpecialty();
        EducationalSpecialty savedEducationalSpecialty = educationalSpecialtyRepository.save(educationalSpecialty);

        log.info("Assigned new students to educational specialty with code name: {}", educationalSpecialtyCodeName);

        return educationalSpecialtyMapper.toDto(savedEducationalSpecialty);
    }

    @Override
    public EducationalSpecialtyDto deleteStudents(
            @NotNull String educationalSpecialtyCodeName,
            @NotEmpty Set<UUID> studentsIds
    ) {
        EducationalSpecialty educationalSpecialty = extractEntityById(
                educationalSpecialtyCodeName,
                educationalSpecialtyRepository,
                codeName -> new EducationalSpecialtyException("Educational specialty with code name: " + codeName + " not found.")
        );

        List<Student> students = extractEntitiesFromIds(studentsIds, studentRepository);
        educationalSpecialty.deleteStudents(new HashSet<>(students));
        educationalSpecialty.associateAllInjectedEntitiesWithEducationalSpecialty();
        EducationalSpecialty savedEducationalSpecialty = educationalSpecialtyRepository.save(educationalSpecialty);

        log.info("Deleted students from educational specialty with code name: {}", educationalSpecialtyCodeName);

        return educationalSpecialtyMapper.toDto(savedEducationalSpecialty);
    }

    @Override
    @Transactional
    public EducationalSpecialtyDto assignNewTeachers(
            @NotNull String educationalSpecialtyCodeName,
            @NotEmpty Set<UUID> teachersIds
    ) {
        EducationalSpecialty educationalSpecialty = extractEntityById(
                educationalSpecialtyCodeName,
                educationalSpecialtyRepository,
                codeName -> new EducationalSpecialtyException("Educational specialty with code name: " + codeName + " not found.")
        );

        List<Teacher> teachers = extractEntitiesFromIds(teachersIds, teacherRepository);
        educationalSpecialty.addTeachers(new HashSet<>(teachers));
        educationalSpecialty.associateAllInjectedEntitiesWithEducationalSpecialty();
        EducationalSpecialty savedEducationalSpecialty = educationalSpecialtyRepository.save(educationalSpecialty);

        log.info("Assigned new teachers to educational specialty with code name: {}", educationalSpecialtyCodeName);

        return educationalSpecialtyMapper.toDto(savedEducationalSpecialty);
    }

    @Override
    public EducationalSpecialtyDto deleteTeachers(
            @NotNull String educationalSpecialtyCodeName,
            @NotEmpty Set<UUID> teachersIds
    ) {
        EducationalSpecialty educationalSpecialty = extractEntityById(
                educationalSpecialtyCodeName,
                educationalSpecialtyRepository,
                codeName -> new EducationalSpecialtyException("Educational specialty with code name: " + codeName + " not found.")
        );

        List<Teacher> teachers = extractEntitiesFromIds(teachersIds, teacherRepository);
        educationalSpecialty.deleteTeachers(new HashSet<>(teachers));
        educationalSpecialty.associateAllInjectedEntitiesWithEducationalSpecialty();
        EducationalSpecialty savedEducationalSpecialty = educationalSpecialtyRepository.save(educationalSpecialty);

        log.info("Deleted teachers from educational specialty with code name: {}", educationalSpecialtyCodeName);

        return educationalSpecialtyMapper.toDto(savedEducationalSpecialty);
    }

    @Override
    @Transactional
    public EducationalSpecialtyDto assignNewTeachingAssigments(
            @NotNull String educationalSpecialtyCodeName,
            @NotEmpty Set<UUID> teachingAssigmentsIds
    ) {
        EducationalSpecialty educationalSpecialty = extractEntityById(
                educationalSpecialtyCodeName,
                educationalSpecialtyRepository,
                codeName -> new EducationalSpecialtyException("Educational specialty with code name: " + codeName + " not found.")
        );

        List<TeachingAssigment> teachingAssigments = extractEntitiesFromIds(teachingAssigmentsIds, teachingAssigmentRepository);
        educationalSpecialty.addTeachingAssigments(new HashSet<>(teachingAssigments));
        educationalSpecialty.associateAllInjectedEntitiesWithEducationalSpecialty();
        EducationalSpecialty savedEducationalSpecialty = educationalSpecialtyRepository.save(educationalSpecialty);

        log.info("Assigned new teaching assigments to educational specialty with code name: {}", educationalSpecialtyCodeName);

        return educationalSpecialtyMapper.toDto(savedEducationalSpecialty);
    }

    @Override
    public EducationalSpecialtyDto deleteTeachingAssigments(
            @NotNull String educationalSpecialtyCodeName,
            @NotEmpty Set<UUID> teachingAssigmentsIds
    ) {
        EducationalSpecialty educationalSpecialty = extractEntityById(
                educationalSpecialtyCodeName,
                educationalSpecialtyRepository,
                codeName -> new EducationalSpecialtyException("Educational specialty with code name: " + codeName + " not found.")
        );

        List<TeachingAssigment> teachingAssigments = extractEntitiesFromIds(teachingAssigmentsIds, teachingAssigmentRepository);
        educationalSpecialty.deleteTeachingAssigments(new HashSet<>(teachingAssigments));
        educationalSpecialty.associateAllInjectedEntitiesWithEducationalSpecialty();
        EducationalSpecialty savedEducationalSpecialty = educationalSpecialtyRepository.save(educationalSpecialty);

        log.info("Deleted teaching assigments from educational specialty with code name: {}", educationalSpecialtyCodeName);

        return educationalSpecialtyMapper.toDto(savedEducationalSpecialty);
    }

    private void checkIfEducationalSpecialtyAlreadyExists(EducationalSpecialtyCreationRequest request) {
        if (educationalSpecialtyRepository.existsByCodeName(request.codeName())) {
            throw new EducationalSpecialtyException("Educational specialty with code name: " +
                    request.codeName() + " already exists.");
        }
    }
}