package be.dpa.bootiful.activities.sadp.jpa;

import be.dpa.bootiful.activities.sadp.jpa.entities.ParticipantEntity;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 * Participant repository.
 *
 * @author denis
 */
@Repository
public interface IParticipantRepository extends PagingAndSortingRepository<ParticipantEntity, Long> {
}
