package be.dpa.bootiful.activities.dm.api;

import java.util.List;
import java.util.Optional;

/**
 * Provides access to the activity domain layer.
 *
 * @author denis
 */
public interface IActivityService {

    /**
     * Gets a list of activities.
     *
     * @return a list opf activities
     */
    List<ActivityResponse> getActivities();

    /**
     * Gets an activity using its alternate key.
     *
     * @param alternateKey the alternate key
     *
     * @return an optional activity
     */
    Optional<ActivityResponse> getActivityBy(String alternateKey);

    /**
     * Creates a new activity using the passed activity request.
     *
     * @param activityRequest the activity request
     *
     * @return the newly created activity
     */
    ActivityResponse newActivity(ActivityRequest activityRequest);

    /**
     * Updates an existing activity.
     *
     * @param alternateKey the alternate key of the activity to update
     * @param activityRequest the activity request containing the data to set
     *
     * @return the updated activity
     */
    ActivityResponse updateActivity(String alternateKey,
                                    ActivityRequest activityRequest);

    /**
     * Deletes an activity by its alternate key.
     *
     * @param alternateKey the alternate key of the activity to delete
     */
    void deleteActivity(String alternateKey);
}
