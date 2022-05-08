package be.dpa.bootiful.activities.dm.api;

import lombok.Data;

@Data
public class ActivityRequest {

    String action;

    String type;

    int noOfParticipants;

    String details;
}
