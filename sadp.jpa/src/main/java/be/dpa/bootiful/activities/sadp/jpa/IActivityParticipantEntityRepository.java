package be.dpa.bootiful.activities.sadp.jpa;

import be.dpa.bootiful.activities.sadp.jpa.entities.ActivityParticipantEntity;
import org.springframework.data.repository.CrudRepository;

/**
 * Repository for the mapping between activities and participants.
 *
 * @author denis
 */
public interface IActivityParticipantEntityRepository
        extends CrudRepository<ActivityParticipantEntity, Long> {
}
