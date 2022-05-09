package be.dpa.bootiful.activities.dm.impl;

import be.dpa.bootiful.activities.dm.api.ActivityModel;
import be.dpa.bootiful.activities.dm.api.ActivityRequest;
import be.dpa.bootiful.activities.dm.api.IActivityService;
import be.dpa.bootiful.activities.dm.impl.mapper.IActivityMapper;
import be.dpa.bootiful.activities.dm.spi.ActivityRecord;
import be.dpa.bootiful.activities.dm.spi.IActivityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

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

    private final IActivityMapper activityMapper;

    private final IActivityRepository activityRepository;

    @Override
    public Page<ActivityModel> getActivities(int page, int size) {
        Page<ActivityRecord> activityRecords = activityRepository.getAll(page, size);
        return activityRecords.map(activityMapper::toActivityResponse);
    }

    @Override
    public Optional<ActivityModel> getActivityBy(String alternateKey) {
        Optional<ActivityRecord> optFound = activityRepository.getBy(alternateKey);
        return optFound.map(activityRecord -> activityMapper.toActivityResponse(activityRecord));
    }

    private ActivityModel save(String alternateKey, ActivityRequest activityRequest) {
        ActivityRecord activityRecord = activityMapper.toActivityRecord(activityRequest);
        activityRecord.setAlternateKey(alternateKey);
        return activityMapper.toActivityResponse(activityRepository.save(activityRecord));
    }

    @Override
    public ActivityModel newActivity(ActivityRequest activityRequest) {
        return save(UUID.randomUUID().toString(), activityRequest);
    }

    @Override
    public ActivityModel updateActivity(String alternateKey, ActivityRequest activityRequest) {
        return save(alternateKey, activityRequest);
    }

    @Override
    public void deleteActivity(String alternateKey) {
        activityRepository.delete(alternateKey);
    }


}
