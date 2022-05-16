package be.dpa.bootiful.activities.sadp.jpa;

import be.dpa.bootiful.activities.sadp.jpa.entities.ActivityEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * The activity entity repository.
 *
 * @author denis
 */
@Repository
public interface IActivityEntityRepository extends PagingAndSortingRepository<ActivityEntity, Long>,
        JpaSpecificationExecutor<ActivityEntity> {

    Page<ActivityEntity> findByOrderByTypeAscActionAsc(Pageable pageable);

    Optional<ActivityEntity> findByExternalKey(String externalKey);

    Optional<ActivityEntity> findByAlternateKey(String alternateKey);

    @Transactional
    Long deleteByAlternateKey(String alternateKey);
}
