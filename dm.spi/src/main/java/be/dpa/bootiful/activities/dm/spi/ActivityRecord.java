package be.dpa.bootiful.activities.dm.spi;

import lombok.Data;

@Data
public class ActivityRecord {

    String alternateKey;

    String externalKey;

    String action;

    String type;

    int noOfParticipants;

    String details;
}
