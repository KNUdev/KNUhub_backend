package ua.knu.knudev.integrationtests;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import ua.knu.knudev.integrationtests.config.IntegrationTestsConfig;
import ua.knu.knudev.knuhubcommon.domain.embeddable.FullName;
import ua.knu.knudev.knuhubcommon.domain.embeddable.MultiLanguageField;
import ua.knu.knudev.knuhubcommon.dto.MultiLanguageFieldDto;
import ua.knu.knudev.peoplemanagement.domain.EducationalSpecialty;
import ua.knu.knudev.peoplemanagement.domain.Faculty;
import ua.knu.knudev.peoplemanagement.domain.User;
import ua.knu.knudev.peoplemanagement.repository.EducationalSpecialtyRepository;
import ua.knu.knudev.peoplemanagement.repository.FacultyRepository;
import ua.knu.knudev.peoplemanagement.repository.UserRepository;
import ua.knu.knudev.peoplemanagement.service.FacultyService;
import ua.knu.knudev.peoplemanagementapi.dto.faculty.FacultyDto;
import ua.knu.knudev.peoplemanagementapi.exception.FacultyException;
import ua.knu.knudev.peoplemanagementapi.request.faculty.FacultyChangeRelationsRequest;
import ua.knu.knudev.peoplemanagementapi.request.faculty.FacultyCreationRequest;
import ua.knu.knudev.peoplemanagementapi.request.faculty.FacultyUpdateRequest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
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
    private UserRepository userRepository;

    private Faculty faculty;

    @BeforeEach
    public void setup() {
        faculty = createNewFaculty();
    }

    @AfterEach
    public void tearDown() {
        userRepository.deleteAll();
        educationalSpecialtyRepository.deleteAll();
        facultyRepository.deleteAll();
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

        User u1 = createNewUser();
        User u2 = createNewUser();
        User u3 = createNewUser();
        User u4 = createNewUser();

        List<String> sIdsList = Stream.of(f5, f6, e2).map(EducationalSpecialty::getCodeName).toList();
        List<UUID> uIdsList = Stream.of(u1, u2, u3, u4).map(User::getId).toList();

        return FacultyCreationRequest.builder()
                .facultyName(MultiLanguageFieldDto.builder()
                        .en("Test faculty1")
                        .uk("Тестовий факультет1")
                        .build())
                .educationalSpecialtyIds(sIdsList)
                .userIds(uIdsList)
                .build();
    }

    private FacultyUpdateRequest createValidFacultyUpdateRequest() {
        return FacultyUpdateRequest.builder()
                .facultyId(faculty.getId())
                .newFacultyUkName("Оновлений факультет")
                .newFacultyEnName("Updated faculty")
                .build();
    }

    private Faculty assignEducationalSpecialtiesToFaculty() {
        EducationalSpecialty educationalSpecialty1 = createNewEducationalSpecialty("Test1");
        EducationalSpecialty educationalSpecialty2 = createNewEducationalSpecialty("Test2");
        EducationalSpecialty educationalSpecialty3 = createNewEducationalSpecialty("Test3");

        Set<EducationalSpecialty> educationalSpecialties = Stream.of(
                        educationalSpecialty1,
                        educationalSpecialty2,
                        educationalSpecialty3)
                .collect(Collectors.toSet());

        faculty.addEducationalSpecialties(educationalSpecialties);

        return facultyRepository.save(faculty);
    }

    private Faculty assignUsersToFaculty() {
        User user1 = createNewUser();
        User user2 = createNewUser();
        User user3 = createNewUser();

        Set<User> users = Stream.of(user1, user2, user3).collect(Collectors.toSet());

        faculty.addUsers(users);

        return facultyRepository.save(faculty);
    }

    private FacultyChangeRelationsRequest createValidFacultyChangeRelationsRequest(UUID facultyId) {
        UUID u1 = createNewUser().getId();
        UUID u2 = createNewUser().getId();
        UUID u3 = createNewUser().getId();

        String e1 = createNewEducationalSpecialty("E1").getCodeName();
        String e2 = createNewEducationalSpecialty("E2").getCodeName();

        return FacultyChangeRelationsRequest.builder()
                .facultyId(facultyId)
                .educationalSpecialtyIds(List.of(e1, e2))
                .userIds(List.of(u1, u2, u3))
                .build();
    }

    private Faculty createFacultyWithRelations() {
        Faculty faculty = createNewFaculty();
        faculty = assignEducationalSpecialtiesToFaculty();
        faculty = assignUsersToFaculty();
        return faculty;
    }

    @Nested
    @DisplayName("Create new faculty method tests")
    class CreateNewFacultyScenarios {

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

            EducationalSpecialty specialty = educationalSpecialtyRepository.findById(response.educationalSpecialties()
                    .stream().findAny().get().codeName()).get();
            User user = userRepository.findById(response.users().stream().findAny().get().id()).get();

            assertNotNull(response);
            assertNotNull(response.id());
            assertEquals(request.facultyName().getEn(), response.facultyName().getEn());
            assertEquals(request.facultyName().getUk(), response.facultyName().getUk());
            assertNotNull(specialty);
            assertNotNull(user);
        }

        @Test
        @DisplayName("Should throw exception when trying to create faculty with empty name")
        public void should_ThrowException_When_TryingToCreateFacultyWithEmptyName() {
            assertThrows(FacultyException.class, () -> facultyService.create(FacultyCreationRequest.builder()
                    .facultyName(new MultiLanguageFieldDto("порожньо", "empty"))
                    .build()));
        }
    }

    @Nested
    @DisplayName("Update faculty method scenarious")
    class UpdateFacultyScenarios {

        @Test
        @DisplayName("Should successfully update faculty when provided valid data")
        public void should_SuccessfullyUpdateFaculty_When_ProvidedValidData() {
            FacultyUpdateRequest request = createValidFacultyUpdateRequest();

            FacultyDto response = facultyService.update(request);

            assertNotNull(response);
            assertEquals(request.facultyId(), response.id());
            assertEquals(request.newFacultyEnName(), response.facultyName().getEn());
            assertEquals(request.newFacultyUkName(), response.facultyName().getUk());
            assertTrue(facultyRepository.findById(response.id()).isPresent());
        }

        @Test
        @DisplayName("Should throw exception when trying to update not existing faculty")
        public void should_ThrowException_When_TryingToUpdateNotExistingFaculty() {
            assertThrows(FacultyException.class, () -> facultyService.update(FacultyUpdateRequest.builder()
                    .facultyId(UUID.randomUUID())
                    .newFacultyUkName(null)
                    .newFacultyEnName(null)
                    .build()));
        }
    }

    @Nested
    @Transactional
    @DisplayName("Change faculty relations with other entities scenarios")
    class ChangeFacultyRelationsWithOtherEntitiesScenarios {

        @Test
        @DisplayName("Should successfully associate faculty with other entities when provided valid data")
        public void should_SuccessfullyAssociateFacultyWithOtherEntities_When_ProvidedValidData() {
            Faculty facultyCopy = faculty;

            FacultyChangeRelationsRequest request = createValidFacultyChangeRelationsRequest(facultyCopy.getId());

            FacultyDto response = facultyService.assignNewRelations(request);

            assertNotNull(response);
            assertEquals(facultyCopy.getId(), response.id());
            assertEquals(request.userIds().size(), response.users().size());
            assertEquals(request.educationalSpecialtyIds().size(), response.educationalSpecialties().size());
        }

        @Test
        @DisplayName("Should successfully delete relations with other entities when provided valid data")
        public void should_SuccessfullyDeleteRelationsWithOtherEntities_When_ProvidedValidData() {
            Faculty facultyWithRelations = createFacultyWithRelations();

            FacultyChangeRelationsRequest request = FacultyChangeRelationsRequest.builder()
                    .userIds(List.of(
                            facultyWithRelations.getUsers().iterator().next().getId()
                    ))
                    .educationalSpecialtyIds(List.of(
                            facultyWithRelations.getEducationalSpecialties().iterator().next().getCodeName()
                    ))
                    .facultyId(facultyWithRelations.getId())
                    .build();

            FacultyDto response = facultyService.deleteRelations(request);

            assertNotNull(response);
            assertEquals(facultyWithRelations.getId(), response.id());
            assertEquals(2, response.users().size());
            assertEquals(2, response.educationalSpecialties().size());
        }
    }
}
