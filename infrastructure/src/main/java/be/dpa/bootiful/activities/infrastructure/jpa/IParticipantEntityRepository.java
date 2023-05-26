package be.dpa.bootiful.activities.infrastructure.jpa;

import be.dpa.bootiful.activities.infrastructure.jpa.entities.ParticipantEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 * Participant repository.
 *
 * @author denis
 */
@Repository
public interface IParticipantEntityRepository extends PagingAndSortingRepository<ParticipantEntity, Long> {

    Page<ParticipantEntity> findByOrderByFirstNameAscLastNameAsc(Pageable pageable);
}
