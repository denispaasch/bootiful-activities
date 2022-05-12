package be.dpa.bootiful.activities.dm.impl.mapper;

import be.dpa.bootiful.activities.dm.api.Participant;
import be.dpa.bootiful.activities.dm.spi.ParticipantRecord;
import org.mapstruct.Mapper;

/**
 * Maps participant records to participants.
 *
 * @author denis
 */
@Mapper(componentModel = "spring")
public interface IParticipantMapper {

    Participant toParticipant(ParticipantRecord participantRecord);

}
