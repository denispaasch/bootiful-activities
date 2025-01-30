package be.dpa.bootiful.activities.domain.spi;

/**
 * Activity import repository.
 *
 * @author denis
 */
public interface ActivityImportRepository {

    /**
     * Imports the passed activity record.
     *
     * @param activityRecord the activity record to import
     */
    void importActivity(ActivityRecord activityRecord);
}
