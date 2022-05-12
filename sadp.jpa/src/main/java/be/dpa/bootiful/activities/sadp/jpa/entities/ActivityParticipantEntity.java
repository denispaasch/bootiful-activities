package be.dpa.bootiful.activities.sadp.jpa.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

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
