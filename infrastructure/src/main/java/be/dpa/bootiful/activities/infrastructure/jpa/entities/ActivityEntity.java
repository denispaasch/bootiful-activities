package be.dpa.bootiful.activities.infrastructure.jpa.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.NaturalId;

import java.util.Objects;
import java.util.Set;

/**
 * The activity entity.
 *
 * @author denis
 */
@Entity(name = "activity")
public class ActivityEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NaturalId
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

    public String getExternalKey() {
        return externalKey;
    }

    public void setExternalKey(String externalKey) {
        this.externalKey = externalKey;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getNoOfParticipants() {
        return noOfParticipants;
    }

    public void setNoOfParticipants(Integer noOfParticipants) {
        this.noOfParticipants = noOfParticipants;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public Set<ActivityParticipantEntity> getParticipantAssignments() {
        return participantAssignments;
    }

    public void setParticipantAssignments(Set<ActivityParticipantEntity> participantAssignments) {
        this.participantAssignments = participantAssignments;
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
        return "ActivityEntity(id=" + this.getId()
                + ", alternateKey=" + this.getAlternateKey()
                + ", externalKey=" + this.getExternalKey()
                + ", action=" + this.getAction()
                + ", type=" + this.getType()
                + ", noOfParticipants=" + this.getNoOfParticipants()
                + ", details=" + this.getDetails() + ")";
    }
}
