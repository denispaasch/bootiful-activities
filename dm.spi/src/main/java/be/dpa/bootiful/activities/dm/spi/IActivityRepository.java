package be.dpa.bootiful.activities.dm.spi;

import java.util.List;
import java.util.Optional;

/**
 * The activity repository providing access to the secondary adapter storage infrastructure.
 *
 * @author denis
 */
public interface IActivityRepository {

    List<ActivityRecord> getAll();

    Optional<ActivityRecord> getBy(String alternateKey);

    ActivityRecord saveExternal(ActivityRecord activity);

    ActivityRecord save(ActivityRecord activity);

    void delete(String alternateKey);
}
