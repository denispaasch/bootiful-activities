package be.dpa.bootiful.activities.sadp.jpa.entities;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

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
    private Long id;

    @NotNull(message = "The alternate key is mandatory")
    @Size(min = 1, max = 255)
    private String alternateKey;

    private String externalKey;

    @NotNull(message = "The action is mandatory")
    @Size(min = 1, max = 255)
    private String action;

    @NotNull(message = "The type is mandatory")
    @Size(min = 1, max = 50)
    private String type;

    @NotNull(message = "The number of participants is mandatory")
    private Integer noOfParticipants;

    @Size(max = 255)
    private String details;

    @ManyToMany(mappedBy = "activities")
    private Set<ParticipantEntity> participants;
}
