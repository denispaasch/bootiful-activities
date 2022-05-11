package be.dpa.bootiful.activities.sadp.jpa.entities;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

/**
 * Participant entity.
 *
 * @author denis
 */
@Entity(name = "participant")
@Data
public class ParticipantEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "The alternate key is mandatory")
    @Size(min = 1, max = 255)
    private String alternateKey;

    @NotNull(message = "The first name is mandatory")
    @Size(min = 1, max = 255)
    private String firstName;

    @NotNull(message = "The last name is mandatory")
    @Size(min = 1, max = 255)
    private String lastName;

    @ManyToMany
    @JoinTable(
            name = "activity_participant",
            joinColumns = @JoinColumn(name = "participant_id"),
            inverseJoinColumns = @JoinColumn(name = "activity_id"))
    private Set<ActivityEntity> activities;
}
