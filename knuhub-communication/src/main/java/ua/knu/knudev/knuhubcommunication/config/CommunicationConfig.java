package ua.knu.knudev.knuhubcommunication.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@ComponentScan("ua.knu.knudev.knuhubcommunication")
@EnableJpaRepositories(basePackages = "ua.knu.knudev.knuhubcommunication.repo")
@EntityScan("ua.knu.knudev.knuhubcommunication.domain")
public class CommunicationConfig {
}
