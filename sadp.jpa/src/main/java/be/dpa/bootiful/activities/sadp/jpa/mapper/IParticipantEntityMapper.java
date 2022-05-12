package be.dpa.bootiful.activities.sadp.jpa.mapper;

import be.dpa.bootiful.activities.dm.spi.ParticipantRecord;
import be.dpa.bootiful.activities.sadp.jpa.ParticipantRepository;
import be.dpa.bootiful.activities.sadp.jpa.entities.ParticipantEntity;
import org.mapstruct.Mapper;

/**
 * Maps participant entities to participant records.
 */
@Mapper(componentModel = "spring")
public interface IParticipantEntityMapper {

    ParticipantRecord toParticipantRecord(ParticipantEntity participantEntity);

}
