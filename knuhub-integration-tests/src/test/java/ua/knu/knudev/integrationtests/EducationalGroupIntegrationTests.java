//package ua.knu.knudev.integrationtests;
//
//import lombok.extern.slf4j.Slf4j;
//import org.junit.jupiter.api.*;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.ActiveProfiles;
//import ua.knu.knudev.integrationtests.config.IntegrationTestsConfig;
//import ua.knu.knudev.peoplemanagement.repository.*;
//import ua.knu.knudev.peoplemanagement.service.EducationalGroupService;
//
//@Slf4j
//@SpringBootTest(classes = IntegrationTestsConfig.class)
//@ActiveProfiles("test")
//public class EducationalGroupIntegrationTests {
//
//    @Autowired
//    private EducationalGroupService educationalGroupService;
//    @Autowired
//    private EducationalGroupRepository educationalGroupRepository;
//    @Autowired
//    private StudentRepository studentRepository;
//    @Autowired
//    private TeacherRepository teacherRepository;
//    @Autowired
//    private SubjectRepository subjectRepository;
//    @Autowired
//    private EducationalSpecialtyRepository educationalSpecialtyRepository;
//
//    @BeforeEach
//    public void setUp() {
//    }
//
//    @AfterEach
//    public void tearDown() {
//        educationalGroupRepository.deleteAll();
//        studentRepository.deleteAll();
//        teacherRepository.deleteAll();
//        subjectRepository.deleteAll();
//        educationalSpecialtyRepository.deleteAll();
//    }
//
//    @Nested
//    @DisplayName("Create Educational Group Scenarios")
//    class CreateEducationalGroupScenarios {
//
//        @Test
//        @DisplayName("Should create an educational group successfully when provided with valid data")
//        public void should_CreateEducationalGroupSuccessfully_When_ProvidedValidData() {
//
//        }
//    }
//}
