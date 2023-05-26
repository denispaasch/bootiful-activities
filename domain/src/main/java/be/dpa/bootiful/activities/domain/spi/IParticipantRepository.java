package be.dpa.bootiful.activities.domain.spi;

import org.springframework.data.domain.Page;

/**
 * Participant repository.
 *
 * @author denis
 */
public interface IParticipantRepository {

    Page<ParticipantRecord> getAll(int page, int size);
}
