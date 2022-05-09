package be.dpa.bootiful.activities.sadp.bored;

import lombok.Data;

/**
 * A bored activity record holds the structure used by the bored API.
 *
 * @author denis
 */
@Data
public class BoredActivityRecord {

    String activity;

    String type;

    int participants;

    String key;

    String link;

}
