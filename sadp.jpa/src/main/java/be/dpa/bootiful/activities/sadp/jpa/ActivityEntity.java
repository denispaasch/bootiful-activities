package be.dpa.bootiful.activities.sadp.jpa;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

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

    String alternateKey;

    String externalKey;

    String action;

    String type;

    Integer noOfParticipants;

    String details;
}
