package ua.knu.knudev.integrationtests;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import ua.knu.knudev.integrationtests.config.IntegrationTestsConfig;
import ua.knu.knudev.knuhubcommon.constant.AnswersRevealTime;
import ua.knu.knudev.knuhubeducation.domain.TestDomain;
import ua.knu.knudev.knuhubeducation.repository.TestRepository;
import ua.knu.knudev.knuhubeducation.service.TestService;
import ua.knu.knudev.knuhubeducationapi.dto.TestDto;
import ua.knu.knudev.knuhubeducationapi.request.TestCreationRequest;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Slf4j
@SpringBootTest(classes = IntegrationTestsConfig.class)
@ActiveProfiles("test")
public class TestServiceIntegrationTest {

    @Autowired
    private TestService testService;
    @Autowired
    private TestRepository testRepository;

    private TestDomain test;

    @BeforeEach
    public void setup() {
        test = createNewTest();
    }

    @AfterEach
    public void teardown() {
        testRepository.deleteAll();
    }

    private TestDomain createNewTest() {
        TestDomain test = TestDomain.builder()
                .title("Test")
                .description("Description")
                .isProtectedMode(false)
                .answersRevealTime(AnswersRevealTime.ON_SUBMIT)
                .deadline(LocalDateTime.of(2030, 1, 1, 1, 1))
                .durationMinutes(60)
                .creatorId(UUID.randomUUID())
                .createdAt(LocalDateTime.now())
                .build();

        return testRepository.save(test);
    }

    @Nested
    @DisplayName("Create new test")
    class CreateNewTestScenarios {

        @Test
        @DisplayName("Should successfully create new test when provided only necessary fields")
        public void should_successfullyCreateNewTest_When_providedNecessaryFields() {
            TestCreationRequest request = TestCreationRequest.builder()
                    .title("Test")
                    .description("Description")
                    .isProtectedMode(false)
                    .answersRevealTime(AnswersRevealTime.ON_SUBMIT)
                    .durationMinutes(60)
                    .creatorId(UUID.randomUUID())
                    .build();

            TestDto response = testService.createTest(request);

            assertNotNull(response);
            assertNotNull(response.id());
            assertEquals(request.title(), response.title());
            assertEquals(request.description(), response.description());
            assertEquals(request.isProtectedMode(), response.isProtectedMode());
            assertEquals(request.answersRevealTime(), response.answersRevealTime());
            assertEquals(request.durationMinutes(), response.durationMinutes());
            assertEquals(request.creatorId(), response.creatorId());
        }
    }
}
