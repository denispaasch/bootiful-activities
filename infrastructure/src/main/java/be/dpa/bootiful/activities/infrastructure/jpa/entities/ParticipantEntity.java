package be.dpa.bootiful.activities.infrastructure.jpa.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.NaturalId;

import java.util.Objects;

/**
 * Participant entity.
 *
 * @author denis
 */
@Entity(name = "participant")
public class ParticipantEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NaturalId
    @NotNull(message = "The alternate key is mandatory")
    @Size(min = 1, max = 255)
    private String alternateKey;

    @NotNull(message = "The first name is mandatory")
    @Size(min = 1, max = 255)
    private String firstName;

    @NotNull(message = "The last name is mandatory")
    @Size(min = 1, max = 255)
    private String lastName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAlternateKey() {
        return alternateKey;
    }

    public void setAlternateKey(String alternateKey) {
        this.alternateKey = alternateKey;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Override
    public int hashCode() {
        return Objects.hash(alternateKey);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        ActivityEntity other = (ActivityEntity) obj;
        return StringUtils.equals(alternateKey, other.getAlternateKey());
    }

    @Override
    public String toString() {
        return "ParticipantEntity(id=" + this.getId()
                + ", alternateKey=" + this.getAlternateKey()
                + ", firstName=" + this.getFirstName()
                + ", lastName=" + this.getLastName() + ")";
    }
}
