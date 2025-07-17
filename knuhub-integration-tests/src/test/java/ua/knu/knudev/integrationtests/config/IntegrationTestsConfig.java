package ua.knu.knudev.integrationtests.config;

import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {
        "ua.knu.knudev.fileservice",
        "ua.knu.knudev.knuhubcommon",
        "ua.knu.knudev.knuhubsecurity"
})
public class IntegrationTestsConfig {
}
