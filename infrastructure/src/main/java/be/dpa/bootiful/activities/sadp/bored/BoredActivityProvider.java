package be.dpa.bootiful.activities.sadp.bored;

import be.dpa.bootiful.activities.dm.spi.ActivityRecord;
import be.dpa.bootiful.activities.dm.spi.IActivityImportRepository;
import be.dpa.bootiful.activities.sadp.bored.mapper.IBoredActivityRecordMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

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

    private final IActivityImportRepository activityImportRepository;

    @Value("${activity.provider.url}")
    private String url;

    @Value("${activity.provider.fetch:10}")
    private int fetchAmount;

    private void fetch(int fetchIx) {
        ResponseEntity<BoredActivityRecord> responseEntity = restTemplate.getForEntity(url, BoredActivityRecord.class);
        ActivityRecord activity = boredActivityMapper.toActivityRecord(responseEntity.getBody());
        log.info("Importing bored activity with type '{}' and action '{}'", activity.getType(), activity.getAction());
        activityImportRepository.importActivity(activity);
    }

    @Async
    public void fetch() {
        IntStream.range(0, fetchAmount).forEach(this::fetch);
    }
}
