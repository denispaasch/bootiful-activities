package be.dpa.bootiful.activities.infrastructure.jpa.entities;

import lombok.Data;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

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
}
