package ua.knu.knudev.knuhubtasks.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@ComponentScan("ua.knu.knudev.knuhubtasks")
@EnableJpaRepositories(basePackages = "ua.knu.knudev.knuhubtasks.repository")
@EntityScan("ua.knu.knudev.knuhubtasks.domain")
public class TasksConfig {
}
