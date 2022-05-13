package be.dpa.bootiful.activities.sadp.bored;

import be.dpa.bootiful.activities.dm.spi.ActivityRecord;
import be.dpa.bootiful.activities.dm.spi.IActivityImportRepository;
import be.dpa.bootiful.activities.sadp.bored.mapper.IBoredActivityRecordMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Fetches bored activities and imports them using the corresponding activity repository.
 *
 * @author denis
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class BoredActivityProvider {

    private final RestTemplate restTemplate;

    private final IBoredActivityRecordMapper boredActivityMapper;

    private final IActivityImportRepository activityImportRepository;

    @Value("${activity.provider.url}")
    private String url;

    @Value("${activity.provider.fetch:10}")
    private int fetchAmount;

    private void fetch() {
        ResponseEntity<BoredActivityRecord> responseEntity = restTemplate.getForEntity(url, BoredActivityRecord.class);
        ActivityRecord activity = boredActivityMapper.toActivityRecord(responseEntity.getBody());
        activityImportRepository.importActivity(activity);
    }

    /**
     * Scheduled fetching of activities from the bored API.
     */
    @PostConstruct
    public void scheduleFetch() {
        TimerTask fetchTask = new TimerTask() {
            private int activityCount = 0;

            @Override
            public void run() {
                fetch();
                activityCount++;
                if (activityCount >= fetchAmount) {
                    cancel();
                }
            }
        };
        Timer timer = new Timer("ActivityProvider-Timer");
        timer.scheduleAtFixedRate(fetchTask, 1000L, 1000L);
    }
}
