package be.dpa.bootiful.activities.sadp.bored;

import be.dpa.bootiful.activities.dm.spi.ActivityRecord;
import be.dpa.bootiful.activities.dm.spi.IActivityRepository;
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
 * Fetches bored activities and saves them using the activity repository.
 *
 * @author denis
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class BoredActivityProvider {

    private final RestTemplate restTemplate = new RestTemplate();

    private final IBoredActivityRecordMapper boredActivityMapper;

    private final IActivityRepository activityRepository;

    @Value("${activity.provider.url}")
    private String url;

    private void fetch() {
        ResponseEntity<BoredActivityRecord> responseEntity = restTemplate.getForEntity(url, BoredActivityRecord.class);
        ActivityRecord activity = boredActivityMapper.toActivityRecord(responseEntity.getBody());
        activityRepository.saveExternal(activity);
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
                if (activityCount >= 10) {
                    cancel();
                }
            }
        };
        Timer timer = new Timer("ActivityProvider-Timer");
        timer.scheduleAtFixedRate(fetchTask, 1000L, 1000L);
    }
}
