package be.dpa.bootiful.activities.dm.spi;

import org.springframework.data.domain.Page;

import java.util.Optional;

/**
 * The activity repository providing access to the secondary adapter storage infrastructure.
 *
 * @author denis
 */
public interface IActivityRepository {

    Page<ActivityRecord> getAll(int page, int size);

    Optional<ActivityRecord> getBy(String alternateKey);

    ActivityRecord save(ActivityRecord activity);

    Long delete(String alternateKey);
}
