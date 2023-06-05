package be.dpa.bootiful.activities.infrastructure.jpa.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/**
 * Mapping between activities and their participants.
 *
 * @author denis
 */
@Entity
@Table(name = "activity_participant")
@Data
@ToString(exclude = {"activity", "participant"})
@EqualsAndHashCode(exclude = {"activity", "participant"})
public class ActivityParticipantEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "activity_id")
    private ActivityEntity activity;

    @ManyToOne
    @JoinColumn(name = "participant_id")
    private ParticipantEntity participant;
}
