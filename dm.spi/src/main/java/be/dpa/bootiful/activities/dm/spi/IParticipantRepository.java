package be.dpa.bootiful.activities.dm.spi;

import org.springframework.data.domain.Page;

/**
 * Participant repository.
 *
 * @author denis
 */
public interface IParticipantRepository {

    Page<ParticipantRecord> getAll(int page, int size);
}
