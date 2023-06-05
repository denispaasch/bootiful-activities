package be.dpa.bootiful.activities.infrastructure.jpa;

import be.dpa.bootiful.activities.domain.spi.ParticipantRecord;
import be.dpa.bootiful.activities.infrastructure.jpa.entities.ParticipantEntity;
import be.dpa.bootiful.activities.infrastructure.jpa.mapper.ParticipantEntityMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;

/**
 * Pariticipant repository.
 *
 * @author denis
 */
@Repository
@RequiredArgsConstructor
public class ParticipantRepository implements be.dpa.bootiful.activities.domain.spi.ParticipantRepository {

    private final ParticipantEntityMapper participantEntityMapper;

    private final ParticipantEntityRepository participantEntityRepository;

    @Override
    public Page<ParticipantRecord> getAll(int page, int size) {
        Page<ParticipantEntity> participantEntities = defaultIfNull(participantEntityRepository
                .findByOrderByFirstNameAscLastNameAsc(PageRequest.of(page, size)), Page.empty());
        return participantEntities.map(participantEntityMapper::toParticipantRecord);
    }
}
