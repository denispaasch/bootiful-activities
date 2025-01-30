package be.dpa.bootiful.activities.infrastructure.bored;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * Triggers the bored activity provider when application is ready.
 *
 * @author denis
 */
@Component
@RequiredArgsConstructor
public class BoredApplicationReadyListener implements ApplicationListener<ApplicationReadyEvent> {

    private final BoredActivityProvider boredActivityProvider;

    @Override
    @Async
    public void onApplicationEvent(ApplicationReadyEvent event) {
        boredActivityProvider.fetch();
    }
}
