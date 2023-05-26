package be.dpa.bootiful.activities.dm.spi;

/**
 * Activity import repository.
 *
 * @author denis
 */
public interface IActivityImportRepository {

    /**
     * Imports the passed activity record.
     *
     * @param activityRecord the activity record to import
     */
    void importActivity(ActivityRecord activityRecord);
}
