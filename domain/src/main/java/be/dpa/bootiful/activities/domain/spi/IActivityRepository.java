package be.dpa.bootiful.activities.domain.spi;

import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

/**
 * The activity repository providing access to the secondary adapter storage infrastructure.
 *
 * @author denis
 */
public interface IActivityRepository {

    Page<ActivityRecord> getAll(Optional<String> search, int page, int size);

    Optional<ActivityRecord> getBy(String alternateKey);

    Page<ParticipantRecord> getParticipantsBy(String alternateKey, int page, int size);

    List<ParticipantRecord> getParticipantsBy(String alternateKey);

    ParticipantRecord newParticipant(String alternateKey, ParticipantRecord participantRecord);

    ActivityRecord save(ActivityRecord activity);

    long delete(String alternateKey);
}
