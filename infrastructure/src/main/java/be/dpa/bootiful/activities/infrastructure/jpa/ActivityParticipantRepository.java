package be.dpa.bootiful.activities.infrastructure.jpa;

import be.dpa.bootiful.activities.infrastructure.jpa.entities.ActivityEntity;
import be.dpa.bootiful.activities.infrastructure.jpa.entities.ActivityParticipantEntity;
import be.dpa.bootiful.activities.infrastructure.jpa.entities.ParticipantEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Activity participant repository.
 *
 * @author denis
 */
@Repository
@RequiredArgsConstructor
public class ActivityParticipantRepository {

    private final ActivityParticipantEntityRepository activityParticipantEntityRepository;

    /**
     * Creates an assignment of a participant to an activity.
     *
     * @param activityEntity the activity entity
     * @param participantEntity the participant entity to assign
     * @return the assignment entity
     */
    public ActivityParticipantEntity createAssignment(ActivityEntity activityEntity,
                                                       ParticipantEntity participantEntity) {
        ActivityParticipantEntity activityParticipantEntity = new ActivityParticipantEntity();
        activityParticipantEntity.setActivity(activityEntity);
        activityParticipantEntity.setParticipant(participantEntity);
        return activityParticipantEntity;
    }

    public ActivityParticipantEntity save(ActivityParticipantEntity activityParticipantEntity) {
        return activityParticipantEntityRepository.save(activityParticipantEntity);
    }

    public void saveAll(List<ActivityParticipantEntity> activityParticipantEntities) {
        activityParticipantEntityRepository.saveAll(activityParticipantEntities);
    }

}
