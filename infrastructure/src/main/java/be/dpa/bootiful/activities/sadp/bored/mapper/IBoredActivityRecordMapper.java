package be.dpa.bootiful.activities.sadp.bored.mapper;

import be.dpa.bootiful.activities.dm.spi.ActivityRecord;
import be.dpa.bootiful.activities.sadp.bored.BoredActivityRecord;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

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
    ActivityRecord toActivityRecord(BoredActivityRecord boredActivity);

    @AfterMapping
    default void appendAlternateKey(@MappingTarget ActivityRecord activityRecord) {
        activityRecord.setAlternateKey(UUID.randomUUID().toString());
    }
}
