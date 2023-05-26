package be.dpa.bootiful.activities.domain.spi;

import lombok.Data;

/**
 * A participant record.
 *
 * @author denis
 */
@Data
public class ParticipantRecord {

    String alternateKey;

    String firstName;

    String lastName;
}
