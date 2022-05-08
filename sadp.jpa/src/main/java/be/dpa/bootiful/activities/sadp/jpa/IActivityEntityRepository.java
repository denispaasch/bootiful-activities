package be.dpa.bootiful.activities.sadp.jpa;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IActivityEntityRepository extends CrudRepository<ActivityEntity, Long> {

    Optional<ActivityEntity> findByExternalKey(String externalKey);

    Optional<ActivityEntity> findByAlternateKey(String alternateKey);
}
