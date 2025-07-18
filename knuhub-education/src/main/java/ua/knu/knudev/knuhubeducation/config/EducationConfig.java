package ua.knu.knudev.knuhubeducation.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@ComponentScan("ua.knu.knudev.knuhubeducation")
@EnableJpaRepositories(basePackages = "ua.knu.knudev.knuhubeducation.repository")
@EntityScan("ua.knu.knudev.knuhubeducation.domain")
public class EducationConfig {
}
