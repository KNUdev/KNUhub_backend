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
import ua.knu.knudev.peoplemanagement.domain.*;
import ua.knu.knudev.peoplemanagement.mapper.educationalSpecialty.EducationalSpecialtyMapper;
import ua.knu.knudev.peoplemanagement.repository.*;
import ua.knu.knudev.peoplemanagementapi.api.EducationalSpecialtyApi;
import ua.knu.knudev.peoplemanagementapi.dto.educationalSpecialty.EducationalSpecialtyDto;
import ua.knu.knudev.peoplemanagementapi.exception.EducationalSpecialtyException;
import ua.knu.knudev.peoplemanagementapi.request.educationalSpecialty.EducationalSpecialtyChangeRelationsRequest;
import ua.knu.knudev.peoplemanagementapi.request.educationalSpecialty.EducationalSpecialtyCreationRequest;
import ua.knu.knudev.peoplemanagementapi.request.educationalSpecialty.EducationalSpecialtyReceivingRequest;
import ua.knu.knudev.peoplemanagementapi.request.educationalSpecialty.EducationalSpecialtyUpdateRequest;

import java.util.HashSet;
import java.util.List;

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
        validateMultiLanguageFields(
                request.name().getEn(),
                request.name().getUk(),
                name -> new EducationalSpecialtyException("Invalid multi-language field: " + name)
        );

        List<Faculty> faculties = extractEntities(request.facultyIds(), facultyRepository);
        List<Student> students = extractEntities(request.studentIds(), studentRepository);
        List<Teacher> teachers = extractEntities(request.teacherIds(), teacherRepository);
        List<EducationalGroup> educationalGroups = extractEntities(request.educationalGroupIds(),
                educationalGroupRepository
        );
        List<TeachingAssigment> teachingAssigments = extractEntities(request.teachingAssigmentIds(),
                teachingAssigmentRepository
        );

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
        EducationalSpecialty educationalSpecialty = getEducationalSpecialtyByCodeName(request.codeName());

        validateMultiLanguageFields(
                request.enSpecialtyName(),
                request.ukSpecialtyName(),
                name -> new EducationalSpecialtyException("Invalid multi-language field: " + name)
        );

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
        EducationalSpecialty educationalSpecialty = getEducationalSpecialtyByCodeName(codeName);

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
    public EducationalSpecialtyDto assignNewRelations(EducationalSpecialtyChangeRelationsRequest request) {
        EducationalSpecialty educationalSpecialty = getEducationalSpecialtyByCodeName(request.codeName());

        if (request.studentIds() != null) {
            List<Student> students = extractEntities(request.studentIds(), studentRepository);
            educationalSpecialty.addStudents(students);
        }
        if (request.teacherIds() != null) {
            List<Teacher> teachers = extractEntities(request.teacherIds(), teacherRepository);
            educationalSpecialty.addTeachers(teachers);
        }
        if (request.educationalGroupIds() != null) {
            List<EducationalGroup> educationalGroups = extractEntities(
                    request.educationalGroupIds(),
                    educationalGroupRepository
            );
            educationalSpecialty.addGroups(educationalGroups);
        }
        if (request.facultyIds() != null) {
            List<Faculty> faculties = extractEntities(request.facultyIds(), facultyRepository);
            educationalSpecialty.addFaculties(faculties);
        }
        if (request.teachingAssignmentIds() != null) {
            List<TeachingAssigment> teachingAssigments = extractEntities(
                    request.teachingAssignmentIds(),
                    teachingAssigmentRepository
            );
            educationalSpecialty.addTeachingAssigments(teachingAssigments);
        }

        educationalSpecialty.associateAllInjectedEntitiesWithEducationalSpecialty();
        EducationalSpecialty savedEducationalSpecialty = educationalSpecialtyRepository.save(educationalSpecialty);

        log.info("Assigned new relations to educational specialty with code name: {}", educationalSpecialty.getCodeName());
        return educationalSpecialtyMapper.toDto(savedEducationalSpecialty);
    }

    @Override
    @Transactional
    public EducationalSpecialtyDto deleteRelations(EducationalSpecialtyChangeRelationsRequest request) {
        EducationalSpecialty educationalSpecialty = getEducationalSpecialtyByCodeName(request.codeName());

        if (request.studentIds() != null) {
            List<Student> students = extractEntities(request.studentIds(), studentRepository);
            educationalSpecialty.deleteStudents(students);
        }
        if (request.teacherIds() != null) {
            List<Teacher> teachers = extractEntities(request.teacherIds(), teacherRepository);
            educationalSpecialty.deleteTeachers(teachers);
        }
        if (request.educationalGroupIds() != null) {
            List<EducationalGroup> educationalGroups = extractEntities(
                    request.educationalGroupIds(),
                    educationalGroupRepository
            );
            educationalSpecialty.deleteGroups(educationalGroups);
        }
        if (request.facultyIds() != null) {
            List<Faculty> faculties = extractEntities(request.facultyIds(), facultyRepository);
            educationalSpecialty.deleteFaculties(faculties);
        }
        if (request.teachingAssignmentIds() != null) {
            List<TeachingAssigment> teachingAssigments = extractEntities(
                    request.teachingAssignmentIds(),
                    teachingAssigmentRepository
            );
            educationalSpecialty.deleteTeachingAssigments(teachingAssigments);
        }

        educationalSpecialty.associateAllInjectedEntitiesWithEducationalSpecialty();
        EducationalSpecialty savedEducationalSpecialty = educationalSpecialtyRepository.save(educationalSpecialty);

        log.info("Deleted relations from educational specialty with code name: {}", educationalSpecialty.getCodeName());
        return educationalSpecialtyMapper.toDto(savedEducationalSpecialty);
    }

    private EducationalSpecialty getEducationalSpecialtyByCodeName(String codeName) {
        return extractEntity(
                codeName,
                educationalSpecialtyRepository,
                id -> new EducationalSpecialtyException("Educational specialty with code name: " + id + " not found.")
        );
    }

    private void checkIfEducationalSpecialtyAlreadyExists(EducationalSpecialtyCreationRequest request) {
        if (educationalSpecialtyRepository.existsByCodeName(request.codeName())) {
            throw new EducationalSpecialtyException("Educational specialty with code name: " +
                    request.codeName() + " already exists.");
        }
    }
}