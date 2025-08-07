package ua.knu.knudev.integrationtests;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import ua.knu.knudev.integrationtests.config.IntegrationTestsConfig;
import ua.knu.knudev.knuhubcommon.domain.embeddable.FullName;
import ua.knu.knudev.knuhubcommon.domain.embeddable.MultiLanguageField;
import ua.knu.knudev.knuhubcommon.dto.MultiLanguageFieldDto;
import ua.knu.knudev.knuhubcommon.mapper.MultiLanguageFieldMapper;
import ua.knu.knudev.peoplemanagement.domain.EducationalGroup;
import ua.knu.knudev.peoplemanagement.domain.EducationalSpecialty;
import ua.knu.knudev.peoplemanagement.domain.Faculty;
import ua.knu.knudev.peoplemanagement.domain.User;
import ua.knu.knudev.peoplemanagement.mapper.FacultyMapper;
import ua.knu.knudev.peoplemanagement.repository.EducationalGroupRepository;
import ua.knu.knudev.peoplemanagement.repository.EducationalSpecialtyRepository;
import ua.knu.knudev.peoplemanagement.repository.FacultyRepository;
import ua.knu.knudev.peoplemanagement.repository.UserRepository;
import ua.knu.knudev.peoplemanagement.service.FacultyService;
import ua.knu.knudev.peoplemanagement.service.HelperService;
import ua.knu.knudev.peoplemanagementapi.dto.FacultyDto;
import ua.knu.knudev.peoplemanagementapi.request.FacultyCreationRequest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest(classes = IntegrationTestsConfig.class)
@ActiveProfiles("test")
public class FacultyServiceIntegrationTests {

    @Autowired
    private FacultyService facultyService;
    @Autowired
    private FacultyRepository facultyRepository;
    @Autowired
    private EducationalSpecialtyRepository educationalSpecialtyRepository;
    @Autowired
    private EducationalGroupRepository educationalGroupRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MultiLanguageFieldMapper multiLanguageFieldMapper;
    @Autowired
    private FacultyMapper facultyMapper;
    @Autowired
    private HelperService helperService;

    private Faculty faculty;
    private EducationalSpecialty educationalSpecialty;
    private EducationalGroup educationalGroup;
    private User user;

    @BeforeEach
    public void setup() {
        user = createNewUser();
        educationalSpecialty = createNewEducationalSpecialty("F7");
        educationalGroup = createNewEducationalGroup();
        faculty = createNewFaculty();
    }

    @AfterEach
    public void tearDown() {
        facultyRepository.deleteAll();
        educationalGroupRepository.deleteAll();
        educationalSpecialtyRepository.deleteAll();
        userRepository.deleteAll();
    }

    private Faculty createNewFaculty() {
        Faculty faculty = new Faculty();

        faculty.setId(UUID.randomUUID());
        faculty.setName(new MultiLanguageField("Test faculty", "Тестовий факультет"));

        return facultyRepository.save(faculty);
    }

    private EducationalSpecialty createNewEducationalSpecialty(String codeName) {
        EducationalSpecialty educationalSpecialty = new EducationalSpecialty();

        String englishSuffix = generateRandomEnglishSuffix();
        String ukrainianSuffix = generateRandomUkrainianSuffix();

        educationalSpecialty.setCodeName(codeName);
        educationalSpecialty.setName(new MultiLanguageField(
                "Specialty:" + englishSuffix,
                "Спеціальність:" + ukrainianSuffix
        ));

        return educationalSpecialtyRepository.save(educationalSpecialty);
    }

    private EducationalGroup createNewEducationalGroup() {
        EducationalGroup educationalGroup = new EducationalGroup();

        String englishSuffix = generateRandomEnglishSuffix();
        String ukrainianSuffix = generateRandomUkrainianSuffix();

        educationalGroup.setId(UUID.randomUUID());
        educationalGroup.setName(new MultiLanguageField(
                "Group:" + englishSuffix,
                "Група:" + ukrainianSuffix
        ));
        return educationalGroupRepository.save(educationalGroup);
    }

    private User createNewUser() {
        User user = new User();

        String englishSuffix = generateRandomEnglishSuffix();
        String ukrainianSuffix = generateRandomUkrainianSuffix();

        FullName fullName = new FullName();
        fullName.setFirstName(new MultiLanguageField("FirstName" + englishSuffix, "Ім'я:" + ukrainianSuffix));
        fullName.setMiddleName(new MultiLanguageField("MiddleName" + englishSuffix, "По-батькові" + ukrainianSuffix));
        fullName.setLastName(new MultiLanguageField("LastName" + englishSuffix, "Прізвище" + ukrainianSuffix));

        user.setId(UUID.randomUUID());
        user.setKnuEmail(englishSuffix + "@knu.ua");
        user.setCommonEmail(englishSuffix + "@gmail.com");
        user.setCreatedAt(LocalDateTime.now());
        user.setFullName(fullName);
        user.setPhoneNumber("+380509730309");

        return userRepository.save(user);
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

    private FacultyCreationRequest createValidFacultyCreationRequestWithNameOnly() {
        return FacultyCreationRequest.builder()
                .facultyName(MultiLanguageFieldDto.builder()
                        .en("Test faculty1")
                        .uk("Тестовий факультет1")
                        .build())
                .build();
    }

    private FacultyCreationRequest createValidFacultyCreationRequest() {
        EducationalSpecialty f5 = createNewEducationalSpecialty("F5");
        EducationalSpecialty f6 = createNewEducationalSpecialty("F6");
        EducationalSpecialty e2 = createNewEducationalSpecialty("E2");

        EducationalGroup g1 = createNewEducationalGroup();
        EducationalGroup g2 = createNewEducationalGroup();
        EducationalGroup g3 = createNewEducationalGroup();

        User u1 = createNewUser();
        User u2 = createNewUser();
        User u3 = createNewUser();
        User u4 = createNewUser();

        List<String> sIdsList = Stream.of(f5, f6, e2).map(EducationalSpecialty::getCodeName).toList();
        List<UUID> gIdsList = Stream.of(g1, g2, g3).map(EducationalGroup::getId).toList();
        List<UUID> uIdsList = Stream.of(u1, u2, u3, u4).map(User::getId).toList();

        return FacultyCreationRequest.builder()
                .facultyName(MultiLanguageFieldDto.builder()
                        .en("Test faculty1")
                        .uk("Тестовий факультет1")
                        .build())
                .educationalSpecialtyIds(sIdsList)
                .educationalGroupIds(gIdsList)
                .userIds(uIdsList)
                .build();
    }

    @Nested
    @DisplayName("Create new faculty method tests")
    class createNewFacultyScenarios {

        @Test
        @DisplayName("Should successfully create new faculty when only name provided")
        public void should_SuccessfullyCreateNewFaculty_When_ProvidedValidDate() {
            FacultyCreationRequest request = createValidFacultyCreationRequestWithNameOnly();

            FacultyDto response = facultyService.create(request);

            assertNotNull(response);
            assertNotNull(response.id());
            assertEquals(request.facultyName().getEn(), response.facultyName().getEn());
            assertEquals(request.facultyName().getUk(), response.facultyName().getUk());
            assertTrue(facultyRepository.findById(response.id()).isPresent());
        }

        @Test
        @DisplayName("Should successfully create new faculty when provided valid data")
        public void should_SuccessfullyCreateNewFaculty_When_ProvidedValidData() {
            FacultyCreationRequest request = createValidFacultyCreationRequest();

            FacultyDto response = facultyService.create(request);
            System.out.println(response);
        }


    }


}
