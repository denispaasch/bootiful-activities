package be.dpa.bootiful.activities.infrastructure.jpa;

import be.dpa.bootiful.activities.infrastructure.jpa.entities.ActivityParticipantEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for the mapping between activities and participants.
 *
 * @author denis
 */
@Repository
public interface ActivityParticipantEntityRepository
        extends PagingAndSortingRepository<ActivityParticipantEntity, Long>,
                CrudRepository<ActivityParticipantEntity, Long> {

    @Query("select ap from ActivityParticipantEntity ap where ap.activity.alternateKey = ?1"
        + " order by ap.participant.firstName, ap.participant.lastName")
    Page<ActivityParticipantEntity> findActivityParticipants(String activityAlternateKey, Pageable pageable);

}
