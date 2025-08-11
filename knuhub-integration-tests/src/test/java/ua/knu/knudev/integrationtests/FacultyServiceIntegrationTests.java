package ua.knu.knudev.integrationtests;

import jakarta.persistence.EntityNotFoundException;
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
import ua.knu.knudev.peoplemanagementapi.dto.EducationalSpecialtyLiteDto;
import ua.knu.knudev.peoplemanagementapi.dto.FacultyDto;
import ua.knu.knudev.peoplemanagementapi.exception.FacultyException;
import ua.knu.knudev.peoplemanagementapi.request.FacultyCreationRequest;
import ua.knu.knudev.peoplemanagementapi.request.FacultyUpdateRequest;

import java.time.LocalDateTime;
import java.util.*;
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
            assertThrows(EntityNotFoundException.class, () -> facultyService.update(FacultyUpdateRequest.builder()
                    .facultyId(UUID.randomUUID())
                    .newFacultyUkName(null)
                    .newFacultyEnName(null)
                    .build()));
        }
    }

    @Nested
    @DisplayName("Assign New Educational Specialty Scenarios")
    @Transactional
    class AssignNewEducationalSpecialtyScenarios {

        @Test
        @DisplayName("Should successfully assign new educational specialty when provided valid data")
        public void should_SuccessfullyAssignNewEducationalSpecialty_When_ProvidedValidData() {
            EducationalSpecialty educationalSpecialty1 = createNewEducationalSpecialty("Test1");
            EducationalSpecialty educationalSpecialty2 = createNewEducationalSpecialty("Test2");

            Set<String> educationalSpecialtiesIds = Stream.of(educationalSpecialty1, educationalSpecialty2)
                    .map(EducationalSpecialty::getCodeName)
                    .collect(Collectors.toSet());

            FacultyDto response = facultyService.assignNewEducationalSpecialties(faculty.getId(), educationalSpecialtiesIds);

            boolean isEducationalSpecialty1WasAdded = response.educationalSpecialties().stream()
                    .anyMatch(educationalSpecialty -> educationalSpecialty.codeName().equals("Test1"));
            boolean isEducationalSpecialty2WasAdded = response.educationalSpecialties().stream()
                    .anyMatch(educationalSpecialty -> educationalSpecialty.codeName().equals("Test2"));

            assertNotNull(response);
            assertTrue(isEducationalSpecialty1WasAdded);
            assertTrue(isEducationalSpecialty2WasAdded);
            assertTrue(response.educationalSpecialties().stream()
                    .map(educationalSpecialtyLiteDto ->
                            educationalSpecialtyRepository.findById(educationalSpecialtyLiteDto.codeName()).get()
                                    .getFaculties().stream()
                                    .anyMatch(f -> f.getId()
                                            .equals(faculty.getId())))
                    .toList()
                    .stream()
                    .allMatch(t -> t == true));
        }

        @Test
        @DisplayName("Should not assign new educational specialty when it was already assigned")
        public void should_NotAssignNewEducationalSpecialty_When_ItWasAlreadyAssigned() {
            EducationalSpecialty f2 = createNewEducationalSpecialty("F2");
            EducationalSpecialty f3 = createNewEducationalSpecialty("F3");
            EducationalSpecialty f1 = createNewEducationalSpecialty("F1");

            facultyService.assignNewEducationalSpecialties(faculty.getId(), Stream.of(f2.getCodeName()).collect(Collectors.toSet()));
            Set<String> ids = Stream.of(f1, f2, f3).map(EducationalSpecialty::getCodeName).collect(Collectors.toSet());
            FacultyDto response = facultyService.assignNewEducationalSpecialties(faculty.getId(), ids);

            Map<String, Long> countByCodeName =
                    response.educationalSpecialties()
                            .stream()
                            .collect(Collectors.groupingBy(
                                    EducationalSpecialtyLiteDto::codeName,
                                    Collectors.counting()
                            ));

            assertEquals(1, countByCodeName.get("F1").intValue());
            assertEquals(1, countByCodeName.get("F2").intValue());
            assertEquals(1, countByCodeName.get("F3").intValue());
        }

    }

    @Nested
    @DisplayName("Delete educational specialty scenarios")
    @Transactional
    class DeleteEducationalSpecialtyScenarios {

        @Test
        @DisplayName("Should successfully delete assigned specialty when provided valid data")
        public void should_SuccessfullyDeleteEducationalSpecialty_When_ProvidedValidData() {
            Faculty faculty = assignEducationalSpecialtiesToFaculty();

            FacultyDto response = facultyService.deleteEducationalSpecialties(
                    faculty.getId(),
                    Stream.of("Test1", "Test3").collect(Collectors.toSet()));

            assertNotNull(response);
            assertEquals(1, response.educationalSpecialties().size());
            assertTrue(educationalSpecialtyRepository.existsByCodeName("Test2"));
            assertTrue(educationalSpecialtyRepository.existsByCodeName("Test3"));
        }
    }

    @Nested
    @DisplayName("Assign Users Scenarios")
    @Transactional
    class AssignUsersScenarios {

        @Test
        @DisplayName("Should successfully assign users to faculty when provided valid data")
        public void should_SuccessfullyAssignUsers_When_ProvidedValidData() {
            User user1 = createNewUser();
            User user2 = createNewUser();

            Set<UUID> userIds = Stream.of(user1, user2)
                    .map(User::getId)
                    .collect(Collectors.toSet());

            FacultyDto response = facultyService.assignNewUsers(faculty.getId(), userIds);

            boolean isUser1Assigned = response.users().stream()
                    .anyMatch(u -> u.id().equals(user1.getId()));
            boolean isUser2Assigned = response.users().stream()
                    .anyMatch(u -> u.id().equals(user2.getId()));

            assertNotNull(response);
            assertTrue(isUser1Assigned);
            assertTrue(isUser2Assigned);
            assertTrue(response.users().stream()
                    .map(userLiteDto ->
                            userRepository.findById(userLiteDto.id()).get()
                                    .getFaculties().stream()
                                    .anyMatch(f -> f.getId()
                                            .equals(faculty.getId())))
                    .toList()
                    .stream()
                    .allMatch(t -> t == true));
        }

        @Test
        @DisplayName("Should not assign users to faculty when they were already assigned")
        public void should_NotAssignUsers_When_TheyWereAlreadyAssigned() {
            Faculty faculty = assignUsersToFaculty();

            Set<UUID> userIds = faculty.getUsers().stream().map(User::getId).collect(Collectors.toSet());

            FacultyDto response = facultyService.assignNewUsers(faculty.getId(), userIds);

            assertEquals(3, response.users().size());
        }
    }

    @Nested
    @DisplayName("Delete Users Scenarios")
    @Transactional
    class DeleteUsersScenarios {

        @Test
        @DisplayName("Should successfully delete assigned users when provided valid data")
        public void should_SuccessfullyDeleteUsers_When_ProvidedValidData() {
            Faculty faculty = assignUsersToFaculty();

            User user = faculty.getUsers().stream().findFirst().get();

            FacultyDto response = facultyService.deleteUsers(faculty.getId(), Stream.of(user.getId()).collect(Collectors.toSet()));

            assertNotNull(response);
            assertEquals(2, response.users().size());
            assertFalse(response.users().stream().anyMatch(u -> u.id().equals(user.getId())));
        }
    }

}
