package be.dpa.bootiful.activities.dm.impl;

import be.dpa.bootiful.activities.dm.api.Activity;
import be.dpa.bootiful.activities.dm.api.ActivityRequest;
import be.dpa.bootiful.activities.dm.api.IActivityService;
import be.dpa.bootiful.activities.dm.api.Participant;
import be.dpa.bootiful.activities.dm.api.ParticipantRequest;
import be.dpa.bootiful.activities.dm.api.exception.ActivityNotFoundException;
import be.dpa.bootiful.activities.dm.api.exception.InvalidParticipantException;
import be.dpa.bootiful.activities.dm.api.exception.ParticipantNotFoundException;
import be.dpa.bootiful.activities.dm.impl.mapper.IActivityMapper;
import be.dpa.bootiful.activities.dm.impl.mapper.IParticipantMapper;
import be.dpa.bootiful.activities.dm.spi.ActivityRecord;
import be.dpa.bootiful.activities.dm.spi.IActivityRepository;
import be.dpa.bootiful.activities.dm.spi.ParticipantRecord;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
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

    private final IActivityMapper activityMapper;

    private final IParticipantMapper participantMapper;

    private final IActivityRepository activityRepository;

    @Override
    public Page<Activity> getActivities(Optional<String> search, int page, int size) {
        Page<ActivityRecord> activityRecords = activityRepository.getAll(search, page, size);
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

    private boolean participantExists(List<ParticipantRecord> activityParticipants,
                                      ParticipantRequest participantRequest) {
        return activityParticipants.stream().filter(activityParticipant ->
                StringUtils.equals(activityParticipant.getFirstName(), participantRequest.getFirstName())
                && StringUtils.equals(activityParticipant.getLastName(), participantRequest.getLastName()))
                .findAny().isPresent();
    }

    private void validateActivity(String activityAk) throws ActivityNotFoundException {
        Optional<ActivityRecord> activityRecord = activityRepository.getBy(activityAk);
        if (!activityRecord.isPresent()) {
            throw new ActivityNotFoundException(
                    String.format("Could not find an activity for the alternate key %s", activityAk));
        }
    }

    @Override
    public Participant newParticipant(String activityAk, ParticipantRequest participantRequest)
        throws ActivityNotFoundException, InvalidParticipantException {
        validateActivity(activityAk);
        List<ParticipantRecord> activityParticipants = activityRepository.getParticipantsBy(activityAk);
        if (participantExists(activityParticipants, participantRequest)) {
            throw new InvalidParticipantException(String.format("The participant %s %s already exists",
                    participantRequest.getFirstName(), participantRequest.getLastName()));
        }

        ParticipantRecord participantRecord = participantMapper.toParticipantRecord(participantRequest);
        return participantMapper.toParticipant(activityRepository.newParticipant(activityAk, participantRecord));
    }

    @Override
    public Participant getParticipantBy(String activityAk, String participantAk)
            throws ActivityNotFoundException, ParticipantNotFoundException {
        validateActivity(activityAk);
        List<ParticipantRecord> participants = activityRepository.getParticipantsBy(activityAk);
        Optional<ParticipantRecord> participantRecord =
                participants.stream().filter(p -> StringUtils.equals(p.getAlternateKey(), participantAk)).findFirst();
        if (!participantRecord.isPresent()) {
            throw new ParticipantNotFoundException(
                    String.format("Could not find participant for alternate key %s", participantAk));
        }
        return participantMapper.toParticipant(participantRecord.get());
    }


}
