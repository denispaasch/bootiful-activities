package be.dpa.bootiful.activities.domain.impl.mapper;

import be.dpa.bootiful.activities.domain.api.Participant;
import be.dpa.bootiful.activities.domain.api.ParticipantRequest;
import be.dpa.bootiful.activities.domain.spi.ParticipantRecord;
import org.mapstruct.Mapper;

/**
 * Maps participant records to participants.
 *
 * @author denis
 */
@Mapper(componentModel = "spring")
public interface ParticipantMapper {

    Participant toParticipant(ParticipantRecord participantRecord);

    ParticipantRecord toParticipantRecord(ParticipantRequest participantRequest);

}
