package be.dpa.bootiful.activities.domain.impl;

import be.dpa.bootiful.activities.domain.api.Activity;
import be.dpa.bootiful.activities.domain.api.ActivityRequest;
import be.dpa.bootiful.activities.domain.api.Participant;
import be.dpa.bootiful.activities.domain.api.ParticipantRequest;
import be.dpa.bootiful.activities.domain.api.exception.ActivityNotFoundException;
import be.dpa.bootiful.activities.domain.api.exception.InvalidParticipantException;
import be.dpa.bootiful.activities.domain.api.exception.ParticipantNotFoundException;
import be.dpa.bootiful.activities.domain.impl.mapper.ActivityMapper;
import be.dpa.bootiful.activities.domain.impl.mapper.ParticipantMapper;
import be.dpa.bootiful.activities.domain.spi.ActivityRecord;
import be.dpa.bootiful.activities.domain.spi.ActivityRepository;
import be.dpa.bootiful.activities.domain.spi.ParticipantRecord;
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
public class ActivityService implements be.dpa.bootiful.activities.domain.api.ActivityService {

    private final ActivityMapper activityMapper;

    private final ParticipantMapper participantMapper;

    private final ActivityRepository activityRepository;

    @Override
    public Page<Activity> getActivities(Optional<String> search, int page, int size) {
        Page<ActivityRecord> activityRecords = activityRepository.getAll(search, page, size);
        return activityRecords.map(activityMapper::toActivityResponse);
    }

    @Override
    public Page<Participant> getActivityParticipants(String activityAk, int page, int size) {
        Page<ParticipantRecord> participantRecords =
                activityRepository.getParticipantsBy(activityAk, page, size);
        return participantRecords.map(participantMapper::toParticipant);
    }

    @Override
    public Activity getActivityBy(String activityAk) throws ActivityNotFoundException {
        Optional<ActivityRecord> optFound = activityRepository.getBy(activityAk);
        if (!optFound.isPresent()) {
            throw new ActivityNotFoundException(
                    String.format("Could not find an activity for the alternate key %s", activityAk));
        }
        return activityMapper.toActivityResponse(optFound.get());
    }

    private Activity save(String alternateKey, ActivityRequest activityRequest) {
        ActivityRecord activityRecord = activityMapper.toActivityRecord(activityRequest, alternateKey);
        return activityMapper.toActivityResponse(activityRepository.save(activityRecord));
    }

    @Override
    public Activity newActivity(ActivityRequest activityRequest) {
        return save(UUID.randomUUID().toString(), activityRequest);
    }

    @Override
    public Activity updateActivity(String activityAk, ActivityRequest activityRequest) {
        return save(activityAk, activityRequest);
    }

    @Override
    public void deleteActivity(String activityAk) throws ActivityNotFoundException {
        long rowsAffected = activityRepository.delete(activityAk);
        if (rowsAffected == 0L) {
            throw new ActivityNotFoundException(
                    String.format("Could not find an activity for the alternate key %s", activityAk));
        }
    }

    private boolean participantExists(List<ParticipantRecord> activityParticipants,
                                      ParticipantRequest participantRequest) {
        return activityParticipants.stream().anyMatch(activityParticipant ->
                StringUtils.equals(activityParticipant.firstName(), participantRequest.getFirstName())
                && StringUtils.equals(activityParticipant.lastName(), participantRequest.getLastName()));
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
                participants.stream().filter(p -> StringUtils.equals(p.alternateKey(), participantAk)).findFirst();
        if (!participantRecord.isPresent()) {
            throw new ParticipantNotFoundException(
                    String.format("Could not find participant for alternate key %s", participantAk));
        }
        return participantMapper.toParticipant(participantRecord.get());
    }


}
