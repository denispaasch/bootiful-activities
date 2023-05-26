package be.dpa.bootiful.activities.dm.api;


import lombok.Data;
import org.springframework.hateoas.RepresentationModel;

/**
 * Activity.
 *
 * @author denis
 */
@Data
public class Activity extends RepresentationModel<Activity> {

    private String alternateKey;

    private String action;

    private String type;

    private int noOfParticipants;

    private String details;
}
