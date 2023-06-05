package be.dpa.bootiful.activities.infrastructure.jpa.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.Set;

/**
 * The activity entity.
 *
 * @author denis
 */
@Entity(name = "activity")
@Data
@ToString(exclude = "participantAssignments")
@EqualsAndHashCode(exclude = "participantAssignments")
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

    @OneToMany(mappedBy = "activity")
    private Set<ActivityParticipantEntity> participantAssignments;
}
