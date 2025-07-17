package ua.knu.knudev.knuhubapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import ua.knu.knudev.fileservice.config.FileServiceModuleConfig;
import ua.knu.knudev.fileserviceapi.config.FileServiceAPIModuleConfig;
import ua.knu.knudev.knuhubcommon.config.CommonConfig;
import ua.knu.knudev.iccliquibase.LiquibaseConfig;
import ua.knu.knudev.knuhubrest.config.RestModuleConfig;
import ua.knu.knudev.knuhubsecurity.config.SecurityModuleConfig;
import ua.knu.knudev.knuhubsecurityapi.config.SecurityApiConfig;

@SpringBootApplication
@Import({FileServiceModuleConfig.class, FileServiceAPIModuleConfig.class, CommonConfig.class, LiquibaseConfig.class,
        RestModuleConfig.class, SecurityModuleConfig.class, SecurityApiConfig.class})
public class KNUhubApplication {
    public static void main(String[] args) {
        SpringApplication.run(KNUhubApplication.class, args);
    }
}