package be.dpa.bootiful.activities.sadp.jpa;

import be.dpa.bootiful.activities.sadp.jpa.entities.ActivityParticipantEntity;
import be.dpa.bootiful.activities.sadp.jpa.entities.ParticipantEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Repository for the mapping between activities and participants.
 *
 * @author denis
 */
public interface IActivityParticipantEntityRepository
        extends PagingAndSortingRepository<ActivityParticipantEntity, Long> {

    @Query("select ap from ActivityParticipantEntity ap where ap.activity.alternateKey = ?1"
        + " order by ap.participant.firstName, ap.participant.lastName")
    Page<ActivityParticipantEntity> findActivityParticipants(String activityAlternateKey, Pageable pageable);

}
