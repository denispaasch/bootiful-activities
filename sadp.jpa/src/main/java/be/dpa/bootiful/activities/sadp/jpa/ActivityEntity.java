package be.dpa.bootiful.activities.sadp.jpa;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * The activity entity.
 *
 * @author denis
 */
@Entity(name = "activity")
@Data
public class ActivityEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @NotNull(message = "The alternate key is mandatory")
    @Size(min = 1, max = 255)
    String alternateKey;

    String externalKey;

    @NotNull(message = "The action is mandatory")
    @Size(min = 1, max = 255)
    String action;

    @NotNull(message = "The type is mandatory")
    @Size(min = 1, max = 50)
    String type;

    @NotNull(message = "The number of participants is mandatory")
    Integer noOfParticipants;

    @Size(max = 255)
    String details;
}
