package ua.knu.knudev.peoplemanagement.service;

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
import ua.knu.knudev.peoplemanagement.mapper.educationalGroup.EducationalGroupMapper;
import ua.knu.knudev.peoplemanagement.repository.*;
import ua.knu.knudev.peoplemanagementapi.api.EducationalGroupApi;
import ua.knu.knudev.peoplemanagementapi.dto.educationalGroup.EducationalGroupDto;
import ua.knu.knudev.peoplemanagementapi.exception.EducationalGroupException;
import ua.knu.knudev.peoplemanagementapi.request.educationalGroup.EducationGroupChangeRelationsRequest;
import ua.knu.knudev.peoplemanagementapi.request.educationalGroup.EducationalGroupCreationRequest;
import ua.knu.knudev.peoplemanagementapi.request.educationalGroup.EducationalGroupReceivingRequest;
import ua.knu.knudev.peoplemanagementapi.request.educationalGroup.EducationalGroupUpdateRequest;

import java.util.HashSet;
import java.util.List;
import java.util.UUID;

import static ua.knu.knudev.knuhubcommon.service.HelperService.*;

@Service
@Slf4j
@RequiredArgsConstructor
@Validated
public class EducationalGroupService implements EducationalGroupApi {

    private final EducationalGroupRepository educationalGroupRepository;
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
    private final SubjectRepository subjectRepository;
    private final EducationalSpecialtyRepository educationalSpecialtyRepository;
    private final EducationalGroupMapper educationalGroupMapper;
    private final MultiLanguageFieldMapper multiLanguageFieldMapper;

    @Override
    public EducationalGroupDto create(
            @Valid EducationalGroupCreationRequest request
    ) {
        validateMultiLanguageFields(
                request.name().getEn(),
                request.name().getUk(),
                message -> new EducationalGroupException("Educational group name is Invalid: " + message)
        );

        MultiLanguageField name = multiLanguageFieldMapper.toDomain(request.name());
        List<Student> students = extractEntities(request.studentsIds(), studentRepository);
        List<Teacher> teachers = extractEntities(request.teachersIds(), teacherRepository);
        List<Subject> subjects = extractEntities(request.subjectsIds(), subjectRepository);
        List<EducationalSpecialty> educationalSpecialties = extractEntities(
                request.educationalSpecialtyIds(),
                educationalSpecialtyRepository
        );

        EducationalGroup educationalGroup = EducationalGroup.builder()
                .name(name)
                .students(new HashSet<>(students))
                .teachers(new HashSet<>(teachers))
                .subjects(new HashSet<>(subjects))
                .educationalSpecialties(new HashSet<>(educationalSpecialties))
                .build();

        educationalGroup.associateAllInjectedEntitiesWithEducationalGroup();
        EducationalGroup savedEducationalGroup = educationalGroupRepository.save(educationalGroup);
        log.info("Created educational group with id: {}", savedEducationalGroup.getId());

        return educationalGroupMapper.toDto(savedEducationalGroup);
    }

    @Override
    public EducationalGroupDto update(
            @Valid EducationalGroupUpdateRequest request
    ) {
        EducationalGroup educationalGroup = getEducationalGroupById(request.educationalGroupId());

        validateMultiLanguageFields(
                request.newEnName(),
                request.newUkName(),
                message -> new EducationalGroupException("Educational group name is Invalid: " + message)
        );

        String enName = getOrDefault(request.newEnName(), educationalGroup.getName().getEn());
        String ukName = getOrDefault(request.newUkName(), educationalGroup.getName().getUk());

        MultiLanguageField newName = new MultiLanguageField(enName, ukName);

        educationalGroup.setName(newName);
        EducationalGroup savedEducationalGroup = educationalGroupRepository.save(educationalGroup);
        log.info("Updated educational group with id: {}", savedEducationalGroup.getId());

        return educationalGroupMapper.toDto(savedEducationalGroup);
    }

    @Override
    public EducationalGroupDto getById(UUID id) {
        EducationalGroup educationalGroup = extractEntity(
                id,
                educationalGroupRepository,
                entityId -> new EducationalGroupException("Educational group with id: " + entityId + " not found")
        );

        log.info("Retrieved educational group with id: {}", educationalGroup.getId());
        return educationalGroupMapper.toDto(educationalGroup);
    }

    @Override
    public void delete(UUID id) {
        educationalGroupRepository.deleteById(id);
    }

    @Override
    public List<EducationalGroupDto> getAll() {
        List<EducationalGroup> educationalGroups = educationalGroupRepository.findAll();

        log.info("Retrieved all educational groups, count: {}", educationalGroups.size());
        return educationalGroupMapper.toDtos(educationalGroups);
    }

    @Override
    public Page<EducationalGroupDto> getFilteredEducationalGroups(
            EducationalGroupReceivingRequest request
    ) {
        Integer pageNumber = getOrDefault(request.pageNumber(), 0);
        Integer pageSize = getOrDefault(request.pageSize(), 10);

        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);

        Page<EducationalGroup> educationalGroups = educationalGroupRepository.getFilteredEducationalGroups(
                request,
                pageRequest
        );

        log.info("Retrieved filtered educational groups, count: {}", educationalGroups.getTotalElements());
        return educationalGroups.map(educationalGroupMapper::toDto);
    }

    @Override
    public EducationalGroupDto assignNewRelations(EducationGroupChangeRelationsRequest request) {
        EducationalGroup educationalGroup = getEducationalGroupById(request.educationalGroupId());

        if (request.studentIds() != null) {
            List<Student> students = extractEntities(request.studentIds(), studentRepository);
            educationalGroup.addStudents(students);
        }
        if (request.teacherIds() != null) {
            List<Teacher> teachers = extractEntities(request.teacherIds(), teacherRepository);
            educationalGroup.addTeachers(teachers);
        }
        if (request.subjectIds() != null) {
            List<Subject> subjects = extractEntities(request.subjectIds(), subjectRepository);
            educationalGroup.addSubjects(subjects);
        }
        if (request.educationalSpecialtyIds() != null) {
            List<EducationalSpecialty> educationalSpecialties = extractEntities(
                    request.educationalSpecialtyIds(),
                    educationalSpecialtyRepository
            );
            educationalGroup.addEducationalSpecialties(educationalSpecialties);
        }

        educationalGroup.associateAllInjectedEntitiesWithEducationalGroup();
        EducationalGroup savedEducationalGroup = educationalGroupRepository.save(educationalGroup);

        log.info("Assigned new relations to educational group with id: {}", savedEducationalGroup.getId());
        return educationalGroupMapper.toDto(savedEducationalGroup);
    }

    @Override
    public EducationalGroupDto deleteRelations(EducationGroupChangeRelationsRequest request) {
        EducationalGroup educationalGroup = getEducationalGroupById(request.educationalGroupId());

        if (request.studentIds() != null) {
            List<Student> students = extractEntities(request.studentIds(), studentRepository);
            educationalGroup.deleteStudents(students);
        }
        if (request.teacherIds() != null) {
            List<Teacher> teachers = extractEntities(request.teacherIds(), teacherRepository);
            educationalGroup.deleteTeachers(teachers);
        }
        if (request.subjectIds() != null) {
            List<Subject> subjects = extractEntities(request.subjectIds(), subjectRepository);
            educationalGroup.deleteSubjects(subjects);
        }
        if (request.educationalSpecialtyIds() != null) {
            List<EducationalSpecialty> educationalSpecialties = extractEntities(
                    request.educationalSpecialtyIds(),
                    educationalSpecialtyRepository
            );
            educationalGroup.deleteEducationalSpecialties(educationalSpecialties);
        }

        educationalGroup.associateAllInjectedEntitiesWithEducationalGroup();
        EducationalGroup savedEducationalGroup = educationalGroupRepository.save(educationalGroup);

        log.info("Deleted relations from educational group with id: {}", savedEducationalGroup.getId());
        return educationalGroupMapper.toDto(savedEducationalGroup);
    }

    private EducationalGroup getEducationalGroupById(UUID educationalGroupId) {
        return extractEntity(
                educationalGroupId,
                educationalGroupRepository,
                id -> new EducationalGroupException("Educational group with id: " + id + " not found")
        );
    }
}
