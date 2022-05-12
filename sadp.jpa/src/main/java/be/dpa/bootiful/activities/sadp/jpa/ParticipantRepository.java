package be.dpa.bootiful.activities.sadp.jpa;

import be.dpa.bootiful.activities.dm.spi.IParticipantRepository;
import be.dpa.bootiful.activities.dm.spi.ParticipantRecord;
import be.dpa.bootiful.activities.sadp.jpa.entities.ParticipantEntity;
import be.dpa.bootiful.activities.sadp.jpa.mapper.IParticipantEntityMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

/**
 * Pariticipant repository.
 *
 * @author denis
 */
@Repository
@RequiredArgsConstructor
public class ParticipantRepository implements IParticipantRepository {

    private final IParticipantEntityMapper participantEntityMapper;

    private final IParticipantEntityRepository participantEntityRepository;

    @Override
    public Page<ParticipantRecord> getAll(int page, int size) {
        Page<ParticipantEntity> participantEntities = participantEntityRepository
                .findByOrderByFirstNameAscLastNameAsc(PageRequest.of(page, size));
        return participantEntities.map(participantEntityMapper::toParticipantRecord);
    }
}
