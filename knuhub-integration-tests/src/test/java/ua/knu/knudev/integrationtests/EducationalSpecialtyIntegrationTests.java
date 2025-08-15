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
import ua.knu.knudev.peoplemanagementapi.request.educationalSpecialty.EducationalSpecialtyChangeRelationsRequest;
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

    @Nested
    @DisplayName("Change educational specialty relations scenarios")
    class AssignEducationalSpecialtyRelationsTests {

        @Test
        @DisplayName("Should successfully create relations between educational specialty and other entities when provided valid data")
        public void shouldSuccessfullyCreateRelations_When_ProvidedValidData() {
            EducationalSpecialty educationSpecialtyCopy = educationalSpecialty;

            UUID f1 = createFaculty().getId();
            UUID f2 = createFaculty().getId();

            UUID g1 = createEducationalGroup().getId();
            UUID g2 = createEducationalGroup().getId();

            UUID s1 = createStudent().getId();
            UUID s2 = createStudent().getId();

            UUID t1 = createTeacher().getId();
            UUID t2 = createTeacher().getId();

            UUID ta1 = createTeachingAssigment().getId();
            UUID ta2 = createTeachingAssigment().getId();

            EducationalSpecialtyChangeRelationsRequest request = EducationalSpecialtyChangeRelationsRequest.builder()
                    .codeName(educationSpecialtyCopy.getCodeName())
                    .facultyIds(List.of(f1, f2))
                    .educationalGroupIds(List.of(g1, g2))
                    .studentIds(List.of(s1, s2))
                    .teacherIds(List.of(t1, t2))
                    .teachingAssignmentIds(List.of(ta1, ta2))
                    .build();

            EducationalSpecialtyDto response = educationalSpecialtyService.assignNewRelations(request);

            assertNotNull(response);
            assertEquals(educationSpecialtyCopy.getCodeName(), response.codeName());
            assertEquals(2, response.faculties().size());
            assertEquals(2, response.groups().size());
            assertEquals(2, response.students().size());
            assertEquals(2, response.teachers().size());
            assertEquals(2, response.teachingAssigments().size());
        }
    }

    @Nested
    @DisplayName("Delete educational specialty relations scenarios")
    @Transactional
    class DeleteEducationalSpecialtyRelationsTests {

        @Test
        @DisplayName("Should successfully delete relations between educational specialty and other entities when provided valid data")
        public void shouldSuccessfullyDeleteRelations_When_ProvidedValidData() {
            EducationalSpecialty educationSpecialtyCopy = buildEducationalSpecialtyWithFacultiesAndOtherEntities();

            UUID f1 = educationSpecialtyCopy.getFaculties().iterator().next().getId();
            UUID g1 = educationSpecialtyCopy.getGroups().iterator().next().getId();
            UUID s1 = educationSpecialtyCopy.getStudents().iterator().next().getId();
            UUID t1 = educationSpecialtyCopy.getTeachers().iterator().next().getId();
            UUID ta1 = educationSpecialtyCopy.getTeachingAssigments().iterator().next().getId();

            EducationalSpecialtyChangeRelationsRequest request = EducationalSpecialtyChangeRelationsRequest.builder()
                    .codeName(educationSpecialtyCopy.getCodeName())
                    .facultyIds(List.of(f1))
                    .educationalGroupIds(List.of(g1))
                    .studentIds(List.of(s1))
                    .teacherIds(List.of(t1))
                    .teachingAssignmentIds(List.of(ta1))
                    .build();

            EducationalSpecialtyDto response = educationalSpecialtyService.deleteRelations(request);

            assertNotNull(response);
            assertEquals(educationSpecialtyCopy.getCodeName(), response.codeName());
            assertEquals(1, response.faculties().size());
            assertEquals(1, response.groups().size());
            assertEquals(1, response.students().size());
            assertEquals(1, response.teachers().size());
            assertEquals(1, response.teachingAssigments().size());
        }
    }
}