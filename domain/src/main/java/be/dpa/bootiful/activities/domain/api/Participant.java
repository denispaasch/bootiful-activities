package be.dpa.bootiful.activities.domain.api;

import lombok.Data;
import org.springframework.hateoas.RepresentationModel;

/**
 * Participant.
 *
 * @author denis
 */
@Data
public class Participant extends RepresentationModel<Participant> {

    private String alternateKey;

    private String firstName;

    private String lastName;
}
