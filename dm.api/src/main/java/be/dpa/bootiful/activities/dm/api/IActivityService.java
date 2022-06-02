package be.dpa.bootiful.activities.dm.api;

import be.dpa.bootiful.activities.dm.api.exception.ActivityNotFoundException;
import be.dpa.bootiful.activities.dm.api.exception.InvalidParticipantException;
import be.dpa.bootiful.activities.dm.api.exception.ParticipantNotFoundException;
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
     * @param search an optional search string
     * @param page   the page index
     * @param size   the page size
     * @return a list of activities
     */
    Page<Activity> getActivities(Optional<String> search, int page, int size);

    /**
     * Gets the the participants of a specific activity.
     *
     * @param activityAlternateKey the alternate key of the activity
     * @param page                 the page index
     * @param size                 the page size
     * @return the participants of the passed activity
     */
    Page<Participant> getActivityParticipants(String activityAlternateKey, int page, int size);

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


    /**
     * Adds a participant to an activity.
     *
     * @param alternateKey       the alternate key of the activity
     * @param participantRequest a participant request
     * @return the assigned participant
     * @throws ActivityNotFoundException in case no activity could be found for the passed alternate key
     * @throws InvalidParticipantException in case of an invalid participant
     */
    Participant newParticipant(String alternateKey, ParticipantRequest participantRequest)
        throws ActivityNotFoundException, InvalidParticipantException;

    /**
     * Gets a participant for the passed parameters.
     *
     * @param activityAk the activity alternate key
     * @param participantAk the participant alternate key
     * @return the participant
     * @throws ActivityNotFoundException in case no activity could be found for the passed alternate key
     * @throws ParticipantNotFoundException in case the participant could not be found
     */
    Participant getParticipantBy(String activityAk, String participantAk) throws ActivityNotFoundException,
            ParticipantNotFoundException;
}
