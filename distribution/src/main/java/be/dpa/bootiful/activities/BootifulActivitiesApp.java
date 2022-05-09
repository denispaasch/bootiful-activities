package be.dpa.bootiful.activities;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
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
}