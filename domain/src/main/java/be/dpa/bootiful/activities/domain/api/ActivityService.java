package be.dpa.bootiful.activities.domain.api;

import be.dpa.bootiful.activities.domain.api.exception.ActivityNotFoundException;
import be.dpa.bootiful.activities.domain.api.exception.InvalidParticipantException;
import be.dpa.bootiful.activities.domain.api.exception.ParticipantNotFoundException;
import org.springframework.data.domain.Page;

import java.util.Optional;

/**
 * Provides access to the activity domain layer.
 *
 * @author denis
 */
public interface ActivityService {

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
     * @param activityAk the alternate key of the activity
     * @param page       the page index
     * @param size       the page size
     * @return the participants of the passed activity
     */
    Page<Participant> getActivityParticipants(String activityAk, int page, int size);

    /**
     * Gets an activity using its alternate key.
     *
     * @param activityAk the alternate key
     * @return an optional activity
     * @throws ActivityNotFoundException in case no activity could be found for the passed alternate key
     */
    Activity getActivityBy(String activityAk) throws ActivityNotFoundException;

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
     * @param activityAk      the alternate key of the activity to update
     * @param activityRequest the activity request containing the data to set
     * @return the updated activity
     */
    Activity updateActivity(String activityAk,
                            ActivityRequest activityRequest);

    /**
     * Deletes an activity by its alternate key.
     *
     * @param activityAk the alternate key of the activity to delete
     * @throws ActivityNotFoundException in case no activity could be found for the passed alternate key
     */
    void deleteActivity(String activityAk) throws ActivityNotFoundException;


    /**
     * Adds a participant to an activity.
     *
     * @param activityAk         the alternate key of the activity
     * @param participantRequest a participant request
     * @return the assigned participant
     * @throws ActivityNotFoundException   in case no activity could be found for the passed alternate key
     * @throws InvalidParticipantException in case of an invalid participant
     */
    Participant newParticipant(String activityAk, ParticipantRequest participantRequest)
            throws ActivityNotFoundException, InvalidParticipantException;

    /**
     * Gets a participant for the passed parameters.
     *
     * @param activityAk    the activity alternate key
     * @param participantAk the participant alternate key
     * @return the participant
     * @throws ActivityNotFoundException    in case no activity could be found for the passed alternate key
     * @throws ParticipantNotFoundException in case the participant could not be found
     */
    Participant getParticipantBy(String activityAk, String participantAk) throws ActivityNotFoundException,
            ParticipantNotFoundException;
}
