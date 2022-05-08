package be.dpa.bootiful.activities.dm.api;


import lombok.Data;
import org.springframework.hateoas.RepresentationModel;

@Data
public class ActivityResponse extends RepresentationModel<ActivityResponse> {
    String alternateKey;

    String action;

    String type;

    int noOfParticipants;

    String details;
}
