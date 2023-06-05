package be.dpa.bootiful.activities.infrastructure.jpa.mapper;

import be.dpa.bootiful.activities.domain.spi.ParticipantRecord;
import be.dpa.bootiful.activities.infrastructure.jpa.entities.ParticipantEntity;
import org.mapstruct.Mapper;

/**
 * Maps participant entities to participant records.
 */
@Mapper(componentModel = "spring")
public interface ParticipantEntityMapper {

    ParticipantRecord toParticipantRecord(ParticipantEntity participantEntity);

    ParticipantEntity toParticipantEntity(ParticipantRecord participantRecord);

}
