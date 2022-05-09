package be.dpa.bootiful.activities.dm.api;

import lombok.Data;

/**
 * Request to create or update an activity.
 *
 * @author denis
 */
@Data
public class ActivityRequest {

    /**
     * The actual activity.
     */
    private String action;

    /**
     * The activity type.
     */
    private String type;

    /**
     * The number of participants.
     */
    private int noOfParticipants;

    /**
     * A reference to more details regarding the activity.
     */
    private String details;
}
