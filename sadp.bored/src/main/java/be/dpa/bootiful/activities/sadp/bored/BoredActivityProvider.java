package be.dpa.bootiful.activities.sadp.bored;

import be.dpa.bootiful.activities.dm.spi.ActivityRecord;
import be.dpa.bootiful.activities.dm.spi.IActivityRepository;
import be.dpa.bootiful.activities.sadp.bored.mapper.IBoredActivityMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.Timer;
import java.util.TimerTask;

@Component
@RequiredArgsConstructor
@Slf4j
public class BoredActivityProvider {

    private final RestTemplate restTemplate = new RestTemplate();

    private final IBoredActivityMapper boredActivityMapper;

    private final IActivityRepository activityRepository;

    @Value("${activity.provider.url}")
    private String url;

    private void fetch() {
        ResponseEntity<BoredActivityDTO> responseEntity = restTemplate.getForEntity(url, BoredActivityDTO.class);
        ActivityRecord activity = boredActivityMapper.toActivityRecord(responseEntity.getBody());
        activityRepository.saveExternal(activity);
    }

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
        timer.scheduleAtFixedRate(fetchTask, 1000L , 1000L);
    }
}
