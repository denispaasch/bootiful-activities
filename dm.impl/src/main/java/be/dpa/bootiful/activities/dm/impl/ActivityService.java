package be.dpa.bootiful.activities.dm.impl;

import be.dpa.bootiful.activities.dm.api.Activity;
import be.dpa.bootiful.activities.dm.api.ActivityRequest;
import be.dpa.bootiful.activities.dm.api.IActivityService;
import be.dpa.bootiful.activities.dm.api.Participant;
import be.dpa.bootiful.activities.dm.impl.mapper.IActivityMapper;
import be.dpa.bootiful.activities.dm.impl.mapper.IParticipantMapper;
import be.dpa.bootiful.activities.dm.spi.ActivityRecord;
import be.dpa.bootiful.activities.dm.spi.IActivityRepository;
import be.dpa.bootiful.activities.dm.spi.ParticipantRecord;
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

    private final IParticipantMapper participantMapper;

    private final IActivityRepository activityRepository;

    @Override
    public Page<Activity> getActivities(int page, int size) {
        Page<ActivityRecord> activityRecords = activityRepository.getAll(page, size);
        return activityRecords.map(activityMapper::toActivityResponse);
    }

    @Override
    public Page<Participant> getActivityParticipants(String activityAlternateKey, int page, int size) {
        Page<ParticipantRecord> participantRecords =
                activityRepository.getParticipantsBy(activityAlternateKey, page, size);
        return participantRecords.map(participantMapper::toParticipant);
    }

    @Override
    public Optional<Activity> getActivityBy(String alternateKey) {
        Optional<ActivityRecord> optFound = activityRepository.getBy(alternateKey);
        return optFound.map(activityRecord -> activityMapper.toActivityResponse(activityRecord));
    }

    private Activity save(String alternateKey, ActivityRequest activityRequest) {
        ActivityRecord activityRecord = activityMapper.toActivityRecord(activityRequest);
        activityRecord.setAlternateKey(alternateKey);
        return activityMapper.toActivityResponse(activityRepository.save(activityRecord));
    }

    @Override
    public Activity newActivity(ActivityRequest activityRequest) {
        return save(UUID.randomUUID().toString(), activityRequest);
    }

    @Override
    public Activity updateActivity(String alternateKey, ActivityRequest activityRequest) {
        return save(alternateKey, activityRequest);
    }

    @Override
    public boolean deleteActivity(String alternateKey) {
        return activityRepository.delete(alternateKey) > 0;
    }


}
