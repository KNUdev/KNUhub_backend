package ua.knu.knudev.peoplemanagement.service;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
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

import static ua.knu.knudev.peoplemanagement.service.HelperService.*;

@RequiredArgsConstructor
@Service
@Slf4j
@Validated
public class EducationalSpecialtyService implements EducationalSpecialtyApi {

    private final EducationalSpecialtyRepository educationalSpecialtyRepository;
    private final MultiLanguageFieldMapper multiLanguageFieldMapper;
    private final EducationalSpecialtyMapper educationalSpecialtyMapper;
    private final FacultyRepository facultyRepository;
    private final EducationalGroupRepository educationalGroupRepository;
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
    private final TeachingAssigmentRepository teachingAssigmentRepository;

    @Override
    @Transactional
    public EducationalSpecialtyDto create(@Valid EducationalSpecialtyCreationRequest request) {
        checkIfEducationalSpecialtyAlreadyExists(request);

        List<Faculty> faculties = extractEntitiesFromIds(request.facultyIds(), facultyRepository);
        List<Student> students = extractEntitiesFromIds(request.studentIds(), studentRepository);
        List<Teacher> teachers = extractEntitiesFromIds(request.teacherIds(), teacherRepository);
        List<EducationalGroup> educationalGroups = extractEntitiesFromIds(request.educationalGroupIds(),
                educationalGroupRepository);
        List<TeachingAssigment> teachingAssigments = extractEntitiesFromIds(request.teachingAssigmentIds(),
                teachingAssigmentRepository);

        EducationalSpecialty educationalSpecialty = EducationalSpecialty.builder()
                .codeName(request.codeName())
                .name(multiLanguageFieldMapper.toDomain(request.name()))
                .faculties(new HashSet<>(faculties))
                .students(new HashSet<>(students))
                .teachers(new HashSet<>(teachers))
                .groups(new HashSet<>(educationalGroups))
                .teachingAssigments(new HashSet<>(teachingAssigments))
                .build();

        EducationalSpecialty savedEducationalSpecialty = educationalSpecialtyRepository.save(educationalSpecialty);
        log.info("Created new educational specialty with code name: {}", savedEducationalSpecialty.getCodeName());

        return educationalSpecialtyMapper.toDto(savedEducationalSpecialty);
    }

    @Override
    public EducationalSpecialtyDto update(@Valid EducationalSpecialtyUpdateRequest request) {
        EducationalSpecialty educationalSpecialty = extractEntityById(request.codeName(), educationalSpecialtyRepository);

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
    public EducationalSpecialtyDto getByCodeName(String codeName) {
        EducationalSpecialty educationalSpecialty = extractEntityById(codeName, educationalSpecialtyRepository);
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
            String educationalSpecialtyCodeName,
            Set<@NotNull UUID> facultiesIds
    ) {
        EducationalSpecialty educationalSpecialty = extractEntityById(
                educationalSpecialtyCodeName,
                educationalSpecialtyRepository
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
            String educationalSpecialtyCodeName,
            Set<@NotNull UUID> facultiesIds
    ) {
        EducationalSpecialty educationalSpecialty = extractEntityById(
                educationalSpecialtyCodeName,
                educationalSpecialtyRepository
        );

        List<Faculty> faculties = extractEntitiesFromIds(facultiesIds, facultyRepository);
        educationalSpecialty.deleteFaculties(new HashSet<>(faculties));
        educationalSpecialty.associateAllInjectedEntitiesWithEducationalSpecialty();
        EducationalSpecialty savedEducationalSpecialty = educationalSpecialtyRepository.save(educationalSpecialty);

        log.info("Deleted faculties from educational specialty with code name: {}", educationalSpecialtyCodeName);

        return educationalSpecialtyMapper.toDto(savedEducationalSpecialty);
    }

    @Override
    public EducationalSpecialtyDto assignNewGroups(
            String educationalSpecialtyCodeName,
            Set<@NotNull UUID> groupsIds
    ) {
        EducationalSpecialty educationalSpecialty = extractEntityById(
                educationalSpecialtyCodeName,
                educationalSpecialtyRepository
        );

        List<EducationalGroup> educationalGroups = extractEntitiesFromIds(groupsIds, educationalGroupRepository);
        educationalSpecialty.addGroups(new HashSet<>(educationalGroups));
        educationalSpecialty.associateAllInjectedEntitiesWithEducationalSpecialty();
        EducationalSpecialty savedEducationalSpecialty = educationalSpecialtyRepository.save(educationalSpecialty);

        log.info("Assigned new groups to educational specialty with code name: {}", educationalSpecialtyCodeName);

        return educationalSpecialtyMapper.toDto(savedEducationalSpecialty);
    }

    @Override
    public EducationalSpecialtyDto deleteGroups(
            String educationalSpecialtyCodeName,
            Set<@NotNull UUID> groupsIds
    ) {
        EducationalSpecialty educationalSpecialty = extractEntityById(
                educationalSpecialtyCodeName,
                educationalSpecialtyRepository
        );

        List<EducationalGroup> educationalGroups = extractEntitiesFromIds(groupsIds, educationalGroupRepository);
        educationalSpecialty.deleteGroups(new HashSet<>(educationalGroups));
        educationalSpecialty.associateAllInjectedEntitiesWithEducationalSpecialty();
        EducationalSpecialty savedEducationalSpecialty = educationalSpecialtyRepository.save(educationalSpecialty);

        log.info("Deleted groups from educational specialty with code name: {}", educationalSpecialtyCodeName);

        return educationalSpecialtyMapper.toDto(savedEducationalSpecialty);
    }

    @Override
    public EducationalSpecialtyDto assignNewStudents(
            String educationalSpecialtyCodeName,
            Set<@NotNull UUID> studentsIds
    ) {
        EducationalSpecialty educationalSpecialty = extractEntityById(
                educationalSpecialtyCodeName,
                educationalSpecialtyRepository
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
            String educationalSpecialtyCodeName,
            Set<@NotNull UUID> studentsIds
    ) {
        EducationalSpecialty educationalSpecialty = extractEntityById(
                educationalSpecialtyCodeName,
                educationalSpecialtyRepository
        );

        List<Student> students = extractEntitiesFromIds(studentsIds, studentRepository);
        educationalSpecialty.deleteStudents(new HashSet<>(students));
        educationalSpecialty.associateAllInjectedEntitiesWithEducationalSpecialty();
        EducationalSpecialty savedEducationalSpecialty = educationalSpecialtyRepository.save(educationalSpecialty);

        log.info("Deleted students from educational specialty with code name: {}", educationalSpecialtyCodeName);

        return educationalSpecialtyMapper.toDto(savedEducationalSpecialty);
    }

    @Override
    public EducationalSpecialtyDto assignNewTeachers(
            String educationalSpecialtyCodeName,
            Set<@NotNull UUID> teachersIds
    ) {
        EducationalSpecialty educationalSpecialty = extractEntityById(
                educationalSpecialtyCodeName,
                educationalSpecialtyRepository
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
            String educationalSpecialtyCodeName,
            Set<@NotNull UUID> teachersIds
    ) {
        EducationalSpecialty educationalSpecialty = extractEntityById(
                educationalSpecialtyCodeName,
                educationalSpecialtyRepository
        );

        List<Teacher> teachers = extractEntitiesFromIds(teachersIds, teacherRepository);
        educationalSpecialty.deleteTeachers(new HashSet<>(teachers));
        educationalSpecialty.associateAllInjectedEntitiesWithEducationalSpecialty();
        EducationalSpecialty savedEducationalSpecialty = educationalSpecialtyRepository.save(educationalSpecialty);

        log.info("Deleted teachers from educational specialty with code name: {}", educationalSpecialtyCodeName);

        return educationalSpecialtyMapper.toDto(savedEducationalSpecialty);
    }

    @Override
    public EducationalSpecialtyDto assignNewTeachingAssigments(
            String educationalSpecialtyCodeName,
            Set<@NotNull UUID> teachingAssigmentsIds
    ) {
        EducationalSpecialty educationalSpecialty = extractEntityById(
                educationalSpecialtyCodeName,
                educationalSpecialtyRepository
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
            String educationalSpecialtyCodeName,
            Set<@NotNull UUID> teachingAssigmentsIds
    ) {
        EducationalSpecialty educationalSpecialty = extractEntityById(
                educationalSpecialtyCodeName,
                educationalSpecialtyRepository
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
