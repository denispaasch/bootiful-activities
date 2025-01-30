package be.dpa.bootiful.activities.infrastructure.bored;

import be.dpa.bootiful.activities.domain.spi.ActivityRecord;
import be.dpa.bootiful.activities.domain.spi.ActivityImportRepository;
import be.dpa.bootiful.activities.infrastructure.bored.mapper.IBoredActivityRecordMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;
import java.util.stream.IntStream;

/**
 * Fetches bored activities and imports them using the corresponding activity repository.
 *
 * @author denis
 */
@Component
@RequiredArgsConstructor
@Slf4j
@EnableAsync
public class BoredActivityProvider {

    private final RestTemplate restTemplate;

    private final IBoredActivityRecordMapper boredActivityMapper;

    private final ActivityImportRepository activityImportRepository;

    @Value("${activity.provider.url}")
    private String url;

    @Value("${activity.provider.fetch:10}")
    private int fetchAmount;

    @Value("${activity.provider.enabled:true}")
    private boolean fetchEnabled;

    private void fetch(int fetchIx) {
        ResponseEntity<BoredActivityRecord> responseEntity = restTemplate.getForEntity(url, BoredActivityRecord.class);
        ActivityRecord activity = boredActivityMapper.toActivityRecord(responseEntity.getBody(),
                UUID.randomUUID().toString());
        log.info("Importing bored activity with type '{}' and action '{}'", activity.type(), activity.action());
        activityImportRepository.importActivity(activity);
    }

    /**
     * Asynchronously fetches a specified number of activities from an external provider and processes them.
     * Each activity is retrieved using the configured URL, mapped to an activity record, and then imported
     * via the activity import repository. The number of activities to fetch and whether this operation
     * is enabled is determined by configurable properties.
     * <ul>
     *   <li>If the `fetchEnabled` flag is false, the fetch operation does not execute.</li>
     *   <li>The number of activities to fetch is determined by the `fetchAmount` property.</li>
     * </ul>
     */
    @Async
    public void fetch() {
        if (fetchEnabled) {
            IntStream.range(0, fetchAmount).forEach(this::fetch);
        }
    }
}
