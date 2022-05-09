package be.dpa.bootiful.activities.dm.api;


import lombok.Data;
import org.springframework.hateoas.RepresentationModel;

/**
 * Activity response.
 *
 * @author denis
 */
@Data
public class ActivityResponse extends RepresentationModel<ActivityResponse> {
    /**
     * The unique identifier of the activity.
     */
    private String alternateKey;

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
