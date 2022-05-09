package be.dpa.bootiful.activities.sadp.jpa;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * The activity entity repository.
 *
 * @author denis
 */
@Repository
public interface IActivityEntityRepository extends PagingAndSortingRepository<ActivityEntity, Long> {

    Page<ActivityEntity> findByOrderByTypeAscActionAsc(Pageable pageable);

    Optional<ActivityEntity> findByExternalKey(String externalKey);

    Optional<ActivityEntity> findByAlternateKey(String alternateKey);
}
