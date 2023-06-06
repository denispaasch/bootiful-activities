package be.dpa.bootiful.activities.infrastructure.bored.mapper;

import be.dpa.bootiful.activities.domain.spi.ActivityRecord;
import be.dpa.bootiful.activities.infrastructure.bored.BoredActivityRecord;
import org.mapstruct.AfterMapping;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.Qualifier;

import java.util.UUID;

/**
 * Maps bored activity data transfer objects to activity records.
 *
 * @author denis
 */
@Mapper(componentModel = "spring")
public interface IBoredActivityRecordMapper {

    @Mapping(source = "activity", target = "action")
    @Mapping(source = "key", target = "externalKey")
    @Mapping(source = "participants", target = "noOfParticipants")
    @Mapping(source = "link", target = "details")
    @Mapping(target = "alternateKey", expression = "java(alternateKey)")
    ActivityRecord toActivityRecord(BoredActivityRecord boredActivity, @Context String alternateKey);

}
