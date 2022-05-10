package be.dpa.bootiful.activities;

import org.springdoc.core.GroupedOpenApi;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * Bootiful activities application.
 *
 * @author denis
 */
@SpringBootApplication
@EnableWebMvc
public class BootifulActivitiesApp {

    /**
     * Main entry point.
     *
     * @param args the arguments for the bootiful activities
     */
    public static void main(String[] args) {
        SpringApplication.run(BootifulActivitiesApp.class, args);
    }

    /**
     * Display only controllers I want.
     *
     * @return a grouped open api object holding a path restriction
     */
    @Bean
    public GroupedOpenApi activitiesOpenApi() {
        String[] paths = {"/api/v1/**"};
        return GroupedOpenApi.builder().group("activities").pathsToMatch(paths)
                .build();
    }
}