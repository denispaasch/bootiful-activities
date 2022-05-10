package be.dpa.bootiful.activities.dm.api;

import org.springframework.data.domain.Page;

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
     * @param page      the page index
     * @param size      the page size
     *
     * @return a list opf activities
     */
    Page<Activity> getActivities(int page, int size);

    /**
     * Gets an activity using its alternate key.
     *
     * @param alternateKey the alternate key
     * @return an optional activity
     */
    Optional<Activity> getActivityBy(String alternateKey);

    /**
     * Creates a new activity using the passed activity request.
     *
     * @param activityRequest the activity request
     * @return the newly created activity
     */
    Activity newActivity(ActivityRequest activityRequest);

    /**
     * Updates an existing activity.
     *
     * @param alternateKey    the alternate key of the activity to update
     * @param activityRequest the activity request containing the data to set
     * @return the updated activity
     */
    Activity updateActivity(String alternateKey,
                            ActivityRequest activityRequest);

    /**
     * Deletes an activity by its alternate key.
     *
     * @param alternateKey the alternate key of the activity to delete
     * @return {@code true} if the activity was deleted, otherwise {@code false}
     */
    boolean deleteActivity(String alternateKey);
}
