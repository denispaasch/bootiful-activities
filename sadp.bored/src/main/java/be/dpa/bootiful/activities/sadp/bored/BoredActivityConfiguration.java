package be.dpa.bootiful.activities.sadp.bored;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * Bored activity configuration.
 *
 * @author denis
 */
@Configuration
public class BoredActivityConfiguration {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
