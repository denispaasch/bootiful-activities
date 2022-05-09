package be.dpa.bootiful.activities.dm.impl;

import be.dpa.bootiful.activities.dm.api.ActivityRequest;
import be.dpa.bootiful.activities.dm.api.ActivityResponse;
import be.dpa.bootiful.activities.dm.api.IActivityService;
import be.dpa.bootiful.activities.dm.impl.mapper.ActivityMapper;
import be.dpa.bootiful.activities.dm.spi.ActivityRecord;
import be.dpa.bootiful.activities.dm.spi.IActivityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Activity service.
 *
 * @author denis 
 */
@Service
@RequiredArgsConstructor
public class ActivityService implements IActivityService {

    private final ActivityMapper activityMapper;

    private final IActivityRepository activityRepository;

    @Override
    public List<ActivityResponse> getActivities() {
        List<ActivityRecord> activityRecords = activityRepository.getAll();
        return activityMapper.toActivityResponses(activityRecords);
    }

    @Override
    public Optional<ActivityResponse> getActivityBy(String alternateKey) {
        Optional<ActivityRecord> optFound = activityRepository.getBy(alternateKey);
        return optFound.map(activityRecord -> activityMapper.toActivityResponse(activityRecord));
    }

    private ActivityResponse save(String alternateKey, ActivityRequest activityRequest) {
        ActivityRecord activityRecord = activityMapper.toActivityRecord(activityRequest);
        activityRecord.setAlternateKey(alternateKey);
        return activityMapper.toActivityResponse(activityRepository.save(activityRecord));
    }

    @Override
    public ActivityResponse newActivity(ActivityRequest activityRequest) {
        return save(UUID.randomUUID().toString(), activityRequest);
    }

    @Override
    public ActivityResponse updateActivity(String alternateKey, ActivityRequest activityRequest) {
        return save(alternateKey, activityRequest);
    }

    @Override
    public void deleteActivity(String alternateKey) {
        activityRepository.delete(alternateKey);
    }


}
