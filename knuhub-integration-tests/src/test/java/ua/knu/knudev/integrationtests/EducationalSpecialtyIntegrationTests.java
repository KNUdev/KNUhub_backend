package ua.knu.knudev.integrationtests;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import ua.knu.knudev.integrationtests.config.IntegrationTestsConfig;
import ua.knu.knudev.knuhubcommon.constant.Semester;
import ua.knu.knudev.knuhubcommon.constant.StudyCourse;
import ua.knu.knudev.knuhubcommon.domain.embeddable.FullName;
import ua.knu.knudev.knuhubcommon.domain.embeddable.MultiLanguageField;
import ua.knu.knudev.knuhubcommon.dto.MultiLanguageFieldDto;
import ua.knu.knudev.peoplemanagement.domain.*;
import ua.knu.knudev.peoplemanagement.repository.*;
import ua.knu.knudev.peoplemanagement.service.EducationalSpecialtyService;
import ua.knu.knudev.peoplemanagementapi.dto.educationalSpecialty.EducationalSpecialtyDto;
import ua.knu.knudev.peoplemanagementapi.exception.EducationalSpecialtyException;
import ua.knu.knudev.peoplemanagementapi.request.educationalSpecialty.EducationalSpecialtyCreationRequest;
import ua.knu.knudev.peoplemanagementapi.request.educationalSpecialty.EducationalSpecialtyUpdateRequest;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest(classes = IntegrationTestsConfig.class)
@ActiveProfiles("test")
public class EducationalSpecialtyIntegrationTests {

    @Autowired
    private EducationalSpecialtyService educationalSpecialtyService;
    @Autowired
    private EducationalSpecialtyRepository educationalSpecialtyRepository;
    @Autowired
    private FacultyRepository facultyRepository;
    @Autowired
    private EducationalGroupRepository educationalGroupRepository;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private TeacherRepository teacherRepository;
    @Autowired
    private TeachingAssigmentRepository teachingAssigmentRepository;

    private EducationalSpecialty educationalSpecialty;

    @BeforeEach
    public void setUp() {
        cleanupAllEntities();

        EducationalSpecialty specialty = EducationalSpecialty.builder()
                .codeName("F2")
                .name(new MultiLanguageField("Computer Science", "Комп'ютерні науки"))
                .build();
        educationalSpecialty = educationalSpecialtyRepository.save(specialty);
    }

    @AfterEach
    public void tearDown() {
        cleanupAllEntities();
    }

    private void cleanupAllEntities() {
        teachingAssigmentRepository.deleteAll();
        educationalGroupRepository.deleteAll();
        facultyRepository.deleteAll();
        studentRepository.deleteAll();
        teacherRepository.deleteAll();
        educationalSpecialtyRepository.deleteAll();
    }

    private Faculty createFaculty() {
        Faculty faculty = Faculty.builder()
                .id(UUID.randomUUID())
                .name(new MultiLanguageField("Faculty " + generateRandomEnglishSuffix(),
                        "Факультет " + generateRandomUkrainianSuffix()))
                .build();

        return facultyRepository.save(faculty);
    }

    private EducationalGroup createEducationalGroup() {
        EducationalGroup educationalGroup = EducationalGroup.builder()
                .id(UUID.randomUUID())
                .name(new MultiLanguageField("CE - " + generateRandomEnglishSuffix(),
                        "КІ - " + generateRandomUkrainianSuffix()))
                .build();

        return educationalGroupRepository.save(educationalGroup);
    }

    private Student createStudent() {
        Student student = Student.builder()
                .id(UUID.randomUUID())
                .knuEmail(generateRandomEnglishSuffix() + "@knu.ua")
                .createdAt(LocalDateTime.now())
                .fullName(new FullName(
                        new MultiLanguageField("John " + generateRandomEnglishSuffix(),
                                "Іван " + generateRandomUkrainianSuffix()),
                        new MultiLanguageField("Doe " + generateRandomEnglishSuffix(),
                                "Доу " + generateRandomUkrainianSuffix()),
                        new MultiLanguageField("Smith " + generateRandomEnglishSuffix(),
                                "Сміт " + generateRandomUkrainianSuffix())))
                .phoneNumber("0" + new Random().nextInt(100000000, 999999999))
                .studentCardNumber(generateRandomEnglishSuffix())
                .isHeadman(false)
                .studyCourse(StudyCourse.THIRD_BACHELOR)
                .build();

        return studentRepository.save(student);
    }

    private Teacher createTeacher() {
        Teacher teacher = Teacher.builder()
                .id(UUID.randomUUID())
                .knuEmail(generateRandomEnglishSuffix() + "@knu.ua")
                .createdAt(LocalDateTime.now())
                .fullName(new FullName(
                        new MultiLanguageField("Jane " + generateRandomEnglishSuffix(),
                                "Яна " + generateRandomUkrainianSuffix()),
                        new MultiLanguageField("Doe " + generateRandomEnglishSuffix(),
                                "Доу " + generateRandomUkrainianSuffix()),
                        new MultiLanguageField("Smith " + generateRandomEnglishSuffix(),
                                "Сміт " + generateRandomUkrainianSuffix())))
                .phoneNumber("0" + new Random().nextInt(100000000, 999999999))
                .scientificMotto("Work till you die")
                .build();

        return teacherRepository.save(teacher);
    }

    private TeachingAssigment createTeachingAssigment() {
        TeachingAssigment teachingAssigment = TeachingAssigment.builder()
                .id(UUID.randomUUID())
                .studyCourse(StudyCourse.THIRD_BACHELOR)
                .semester(Semester.FIRST)
                .educationalSpecialty(educationalSpecialty)
                .build();

        return teachingAssigmentRepository.save(teachingAssigment);
    }

    private EducationalSpecialtyCreationRequest createPrimitiveEducationalSpecialtyCreationRequest() {
        return EducationalSpecialtyCreationRequest.builder()
                .codeName("F3")
                .name(new MultiLanguageFieldDto("Physics", "Фізика"))
                .build();
    }

    private String generateRandomEnglishSuffix() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 6; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        return sb.toString();
    }

    private String generateRandomUkrainianSuffix() {
        String chars = "АБВГҐДЕЄЖЗИІЇЙКЛМНОПРСТУФХЦЧШЩЬЮЯ";
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 6; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        return sb.toString();
    }

    private EducationalSpecialty buildEducationalSpecialtyWithFacultiesAndOtherEntities() {
        Faculty f1 = createFaculty();
        Faculty f2 = createFaculty();

        EducationalGroup g1 = createEducationalGroup();
        EducationalGroup g2 = createEducationalGroup();

        Student s1 = createStudent();
        Student s2 = createStudent();

        Teacher t1 = createTeacher();
        Teacher t2 = createTeacher();

        TeachingAssigment ta1 = createTeachingAssigment();
        TeachingAssigment ta2 = createTeachingAssigment();

        EducationalSpecialty specialty = EducationalSpecialty.builder()
                .codeName(UUID.randomUUID().toString())
                .name(new MultiLanguageField("Engineering", "Інженерія"))
                .faculties(new HashSet<>(Set.of(f1, f2)))
                .groups(new HashSet<>(Set.of(g1, g2)))
                .students(new HashSet<>(Set.of(s1, s2)))
                .teachers(new HashSet<>(Set.of(t1, t2)))
                .teachingAssigments(new HashSet<>(Set.of(ta1, ta2)))
                .build();

        return educationalSpecialtyRepository.save(specialty);
    }

    @Nested
    @DisplayName("Create Educational Specialty scenarios")
    class CreateEducationalSpecialtyTests {

        @Test
        @DisplayName("Should successfully create a new educational specialty when provided short valid data")
        public void shouldSuccessfullyCreateEducationalSpecialty_When_ProvidedShortValidData() {
            EducationalSpecialtyCreationRequest request = createPrimitiveEducationalSpecialtyCreationRequest();

            EducationalSpecialtyDto response = educationalSpecialtyService.create(request);

            assertNotNull(response);
            assertTrue(educationalSpecialtyRepository.existsByCodeName("F3"));
            assertEquals(request.codeName(), response.codeName());
            assertEquals(request.name().getEn(), response.name().getEn());
            assertEquals(request.name().getUk(), response.name().getUk());
        }

        @Test
        @DisplayName("Should throw an exception when trying to create an educational specialty with an existing code name")
        public void shouldThrowException_When_CreatingEducationalSpecialtyWithExistingCodeName() {
            EducationalSpecialtyCreationRequest request = EducationalSpecialtyCreationRequest.builder()
                    .codeName(educationalSpecialty.getCodeName())
                    .name(new MultiLanguageFieldDto("New Specialty", "Нова Спеціальність"))
                    .build();

            assertThrows(EducationalSpecialtyException.class, () -> educationalSpecialtyService.create(request));
        }

        @Test
        @DisplayName("Should throw an exception when trying to create an educational specialty with invalid names")
        public void shouldThrowException_When_CreatingEducationalSpecialtyWithInvalidNames() {
            EducationalSpecialtyCreationRequest request = EducationalSpecialtyCreationRequest.builder()
                    .codeName("F4")
                    .name(new MultiLanguageFieldDto("жі", "dld"))
                    .build();

            assertThrows(EducationalSpecialtyException.class, () -> educationalSpecialtyService.create(request));
        }
    }

    @Nested
    @DisplayName("Update Educational Specialty scenarios")
    class UpdateEducationalSpecialtyTests {

        @Test
        @DisplayName("Should successfully update an existing educational specialty when provided valid data")
        public void shouldSuccessfullyUpdateEducationalSpecialty_When_ProvidedValidData() {
            EducationalSpecialtyUpdateRequest request = EducationalSpecialtyUpdateRequest.builder()
                    .codeName(educationalSpecialty.getCodeName())
                    .enSpecialtyName("Changed En")
                    .ukSpecialtyName("Змінена Ук")
                    .build();

            EducationalSpecialtyDto response = educationalSpecialtyService.update(request);

            assertNotNull(response);

            assertEquals(educationalSpecialty.getCodeName(), response.codeName());
            assertEquals("Changed En", response.name().getEn());
            assertEquals("Змінена Ук", response.name().getUk());
        }
    }

    @Test
    @DisplayName("Should successfully delete an existing educational specialty")
    public void shouldSuccessfullyDeleteEducationalSpecialty_When_ProvidedValidId() {
        educationalSpecialtyService.delete(educationalSpecialty.getCodeName());

        assertFalse(educationalSpecialtyRepository.existsByCodeName(educationalSpecialty.getCodeName()));
    }

    @Test
    @DisplayName("Should return an educational specialty by code name when it exists")
    public void shouldReturnEducationalSpecialty_When_ExistsByCodeName() {
        EducationalSpecialtyDto response = educationalSpecialtyService.getByCodeName(educationalSpecialty.getCodeName());

        assertNotNull(response);
        assertEquals(educationalSpecialty.getCodeName(), response.codeName());
        assertEquals(educationalSpecialty.getName().getEn(), response.name().getEn());
        assertEquals(educationalSpecialty.getName().getUk(), response.name().getUk());
    }

    @Test
    @DisplayName("Should successfully assign new faculties to an educational specialty when provided valid data")
    public void shouldSuccessfullyAssignFacultiesToEducationalSpecialty_When_ProvidedValidData() {
        UUID f1 = createFaculty().getId();
        UUID f2 = createFaculty().getId();
        UUID f3 = createFaculty().getId();

        EducationalSpecialtyDto response = educationalSpecialtyService.assignNewFaculties(
                educationalSpecialty.getCodeName(),
                new HashSet<>(Set.of(f1, f2, f3))
        );

        assertNotNull(response);
        assertEquals(3, response.faculties().size());
        assertTrue(response.faculties().stream().anyMatch(faculty -> faculty.id().equals(f1)));
        assertTrue(response.faculties().stream().anyMatch(faculty -> faculty.id().equals(f2)));
        assertTrue(response.faculties().stream().anyMatch(faculty -> faculty.id().equals(f3)));
    }

    @Nested
    @Transactional
    @DisplayName("Should successfully delete assigned faculties from an educational specialty")
    class DeleteAssignedFacultiesFromEducationalSpecialtyTests {

        @Test
        @DisplayName("Should successfully delete assigned faculties from an educational specialty when provided valid data")
        public void shouldSuccessfullyDeleteAssignedFacultiesFromEducationalSpecialty_When_ProvidedValidData() {
            EducationalSpecialty specialty = buildEducationalSpecialtyWithFacultiesAndOtherEntities();

            EducationalSpecialtyDto response = educationalSpecialtyService.deleteFaculties(
                    specialty.getCodeName(),
                    new HashSet<>(Set.of(specialty.getFaculties().iterator().next().getId()))
            );

            assertNotNull(response);
            assertEquals(1, response.faculties().size());
            assertTrue(response.faculties().stream().anyMatch(faculty ->
                    faculty.id().equals(specialty.getFaculties().iterator().next().getId())));
        }

        @Test
        @DisplayName("Should successfully delete all assigned faculties from an educational specialty when provided valid data")
        public void shouldSuccessfullyDeleteAllAssignedFacultiesFromEducationalSpecialty_When_ProvidedValidData() {
            EducationalSpecialty specialty = buildEducationalSpecialtyWithFacultiesAndOtherEntities();

            List<UUID> facultiesIDs = specialty.getFaculties().stream().map(Faculty::getId).toList();

            EducationalSpecialtyDto response = educationalSpecialtyService.deleteFaculties(
                    specialty.getCodeName(),
                    new HashSet<>(facultiesIDs)
            );

            assertNotNull(response);
            assertTrue(response.faculties().isEmpty());

            facultyRepository.findAllById(facultiesIDs).forEach(faculty -> {
                assertNull(faculty.getEducationalSpecialties());
            });
        }
    }

    @Test
    @DisplayName("Should successfully assign new educational groups to an educational specialty when provided valid data")
    public void shouldSuccessfullyAssignEducationalGroupsToEducationalSpecialty_When_ProvidedValidData() {
        UUID g1 = createEducationalGroup().getId();
        UUID g2 = createEducationalGroup().getId();
        UUID g3 = createEducationalGroup().getId();

        EducationalSpecialtyDto response = educationalSpecialtyService.assignNewGroups(
                educationalSpecialty.getCodeName(),
                new HashSet<>(Set.of(g1, g2, g3))
        );

        assertNotNull(response);
        assertEquals(3, response.groups().size());
        assertTrue(response.groups().stream().anyMatch(group -> group.id().equals(g1)));
        assertTrue(response.groups().stream().anyMatch(group -> group.id().equals(g2)));
        assertTrue(response.groups().stream().anyMatch(group -> group.id().equals(g3)));
    }

    @Nested
    @Transactional
    @DisplayName("Should successfully delete assigned educational groups from an educational specialty")
    class DeleteAssignedEducationalGroupsFromEducationalSpecialtyTests {

        @Test
        @DisplayName("Should successfully delete assigned educational groups from an educational specialty when provided valid data")
        public void shouldSuccessfullyDeleteAssignedEducationalGroupsFromEducationalSpecialty_When_ProvidedValidData() {
            EducationalSpecialty specialty = buildEducationalSpecialtyWithFacultiesAndOtherEntities();

            EducationalSpecialtyDto response = educationalSpecialtyService.deleteGroups(
                    specialty.getCodeName(),
                    new HashSet<>(Set.of(specialty.getGroups().iterator().next().getId()))
            );

            assertNotNull(response);
            assertEquals(1, response.groups().size());
            assertTrue(response.groups().stream().anyMatch(group ->
                    group.id().equals(specialty.getGroups().iterator().next().getId())));
        }
    }

    @Test
    @DisplayName("Should successfully assign new students to an educational specialty when provided valid data")
    public void shouldSuccessfullyAssignStudentsToEducationalSpecialty_When_ProvidedValidData() {
        UUID s1 = createStudent().getId();
        UUID s2 = createStudent().getId();
        UUID s3 = createStudent().getId();

        EducationalSpecialtyDto response = educationalSpecialtyService.assignNewStudents(
                educationalSpecialty.getCodeName(),
                new HashSet<>(Set.of(s1, s2, s3))
        );

        assertNotNull(response);
        assertEquals(3, response.students().size());
        assertTrue(response.students().stream().anyMatch(student -> student.id().equals(s1)));
        assertTrue(response.students().stream().anyMatch(student -> student.id().equals(s2)));
        assertTrue(response.students().stream().anyMatch(student -> student.id().equals(s3)));
    }

    @Nested
    @Transactional
    @DisplayName("Should successfully delete assigned students from an educational specialty")
    class DeleteAssignedStudentsFromEducationalSpecialtyTests {
        @Test
        @DisplayName("Should successfully delete assigned students from an educational specialty when provided valid data")
        public void shouldSuccessfullyDeleteAssignedStudentsFromEducationalSpecialty_When_ProvidedValidData() {
            EducationalSpecialty specialty = buildEducationalSpecialtyWithFacultiesAndOtherEntities();

            EducationalSpecialtyDto response = educationalSpecialtyService.deleteStudents(
                    specialty.getCodeName(),
                    new HashSet<>(Set.of(specialty.getStudents().iterator().next().getId()))
            );

            assertNotNull(response);
            assertEquals(1, response.students().size());
            assertTrue(response.students().stream().anyMatch(student ->
                    student.id().equals(specialty.getStudents().iterator().next().getId())));
        }
    }

    @Test
    @DisplayName("Should successfully assign new teachers to an educational specialty when provided valid data")
    public void shouldSuccessfullyAssignTeachersToEducationalSpecialty_When_ProvidedValidData() {
        UUID t1 = createTeacher().getId();
        UUID t2 = createTeacher().getId();
        UUID t3 = createTeacher().getId();

        EducationalSpecialtyDto response = educationalSpecialtyService.assignNewTeachers(
                educationalSpecialty.getCodeName(),
                new HashSet<>(Set.of(t1, t2, t3))
        );

        assertNotNull(response);
        assertEquals(3, response.teachers().size());
        assertTrue(response.teachers().stream().anyMatch(teacher -> teacher.id().equals(t1)));
        assertTrue(response.teachers().stream().anyMatch(teacher -> teacher.id().equals(t2)));
        assertTrue(response.teachers().stream().anyMatch(teacher -> teacher.id().equals(t3)));
    }

    @Nested
    @Transactional
    @DisplayName("Should successfully delete assigned teachers from an educational specialty")
    class DeleteAssignedTeachersFromEducationalSpecialtyTests {

        @Test
        @DisplayName("Should successfully delete assigned teachers from an educational specialty when provided valid data")
        public void shouldSuccessfullyDeleteAssignedTeachersFromEducationalSpecialty_When_ProvidedValidData() {
            EducationalSpecialty specialty = buildEducationalSpecialtyWithFacultiesAndOtherEntities();

            EducationalSpecialtyDto response = educationalSpecialtyService.deleteTeachers(
                    specialty.getCodeName(),
                    new HashSet<>(Set.of(specialty.getTeachers().iterator().next().getId()))
            );

            assertNotNull(response);
            assertEquals(1, response.teachers().size());
            assertTrue(response.teachers().stream().anyMatch(teacher ->
                    teacher.id().equals(specialty.getTeachers().iterator().next().getId())));
        }
    }

    @Test
    @DisplayName("Should successfully assign new teaching assignments to an educational specialty when provided valid data")
    public void shouldSuccessfullyAssignTeachingAssignmentsToEducationalSpecialty_When_ProvidedValidData() {
        TeachingAssigment ta1 = createTeachingAssigment();
        TeachingAssigment ta2 = createTeachingAssigment();
        TeachingAssigment ta3 = createTeachingAssigment();

        EducationalSpecialtyDto response = educationalSpecialtyService.assignNewTeachingAssigments(
                educationalSpecialty.getCodeName(),
                new HashSet<>(Set.of(ta1.getId(), ta2.getId(), ta3.getId()))
        );

        assertNotNull(response);
        assertEquals(3, response.teachingAssigments().size());
        assertTrue(response.teachingAssigments().stream().anyMatch(ta -> ta.id().equals(ta1.getId())));
        assertTrue(response.teachingAssigments().stream().anyMatch(ta -> ta.id().equals(ta2.getId())));
        assertTrue(response.teachingAssigments().stream().anyMatch(ta -> ta.id().equals(ta3.getId())));
    }

    @Nested
    @Transactional
    @DisplayName("Should successfully delete assigned teaching assignments from an educational specialty")
    class DeleteAssignedTeachingAssignmentsFromEducationalSpecialtyTests {

        @Test
        @DisplayName("Should successfully delete assigned teaching assignments from an educational specialty when provided valid data")
        public void shouldSuccessfullyDeleteAssignedTeachingAssignmentsFromEducationalSpecialty_When_ProvidedValidData() {
            EducationalSpecialty specialty = buildEducationalSpecialtyWithFacultiesAndOtherEntities();

            EducationalSpecialtyDto response = educationalSpecialtyService.deleteTeachingAssigments(
                    specialty.getCodeName(),
                    new HashSet<>(Set.of(specialty.getTeachingAssigments().iterator().next().getId()))
            );

            assertNotNull(response);
            assertEquals(1, response.teachingAssigments().size());
            assertTrue(response.teachingAssigments().stream().anyMatch(ta ->
                    ta.id().equals(specialty.getTeachingAssigments().iterator().next().getId())));
        }
    }
}