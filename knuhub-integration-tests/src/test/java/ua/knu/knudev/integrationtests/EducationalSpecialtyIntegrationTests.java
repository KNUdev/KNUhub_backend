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
import ua.knu.knudev.peoplemanagementapi.dto.EducationalSpecialtyDto;
import ua.knu.knudev.peoplemanagementapi.request.EducationalSpecialtyCreationRequest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

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
//        EducationalSpecialty specialty = EducationalSpecialty.builder()
//                .codeName("T" + System.currentTimeMillis())
//                .name(new MultiLanguageField("Temp Specialty", "Тимчасова спеціальність"))
//                .build();
//        specialty = educationalSpecialtyRepository.save(specialty);

        TeachingAssigment teachingAssigment = TeachingAssigment.builder()
                .id(UUID.randomUUID())
                .studyCourse(StudyCourse.THIRD_BACHELOR)
                .semester(Semester.FIRST)
//                .educationalSpecialty(specialty)
                .build();

        return teachingAssigmentRepository.save(teachingAssigment);
    }

    private EducationalSpecialtyCreationRequest createPrimitiveEducationalSpecialtyCreationRequest() {
        return EducationalSpecialtyCreationRequest.builder()
                .codeName("F3")
                .name(new MultiLanguageFieldDto("Physics", "Фізика"))
                .build();
    }

    private EducationalSpecialtyCreationRequest createFullEducationalSpecialtyCreationRequest() {
        String uniqueCodeName = "F" + System.currentTimeMillis();

        List<UUID> facultyIds = new ArrayList<>(List.of(createFaculty().getId()));
        List<UUID> educationGroupsIds = new ArrayList<>(List.of(createEducationalGroup().getId(),
                createEducationalGroup().getId()));
        List<UUID> studentsIds = new ArrayList<>(List.of(createStudent().getId(),
                createStudent().getId(), createStudent().getId(), createStudent().getId()));
        List<UUID> teachersIds = new ArrayList<>(List.of(createTeacher().getId(), createTeacher().getId(),
                createTeacher().getId()));

        List<UUID> teachingAssigmentsIds = new ArrayList<>(List.of(createTeachingAssigment().getId(),
                createTeachingAssigment().getId()));

        return EducationalSpecialtyCreationRequest.builder()
                .codeName(uniqueCodeName)
                .name(new MultiLanguageFieldDto("Chemistry", "Хімія"))
                .facultyIds(facultyIds)
                .educationalGroupIds(educationGroupsIds)
                .studentIds(studentsIds)
                .teacherIds(teachersIds)
                .teachingAssigmentIds(teachingAssigmentsIds)
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

    @Nested
    @DisplayName("Create Educational Specialty scenarios")
    @Transactional
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
        @DisplayName("Should successfully create a new educational specialty when provided full valid data")
        public void shouldSuccessfullyCreateEducationalSpecialty_When_ProvidedFullValidData() {
            EducationalSpecialtyCreationRequest request = createFullEducationalSpecialtyCreationRequest();

            EducationalSpecialtyDto response = educationalSpecialtyService.create(request);

            assertNotNull(response);

            assertTrue(educationalSpecialtyRepository.existsByCodeName("F41"));
            assertEquals(request.codeName(), response.codeName());
            assertEquals(request.name().getEn(), response.name().getEn());
            assertEquals(request.name().getUk(), response.name().getUk());

            assertFalse(response.faculties().isEmpty());
            assertFalse(response.groups().isEmpty());
            assertFalse(response.students().isEmpty());
            assertFalse(response.teachers().isEmpty());
            assertFalse(response.teachingAssigments().isEmpty());
        }
    }
}