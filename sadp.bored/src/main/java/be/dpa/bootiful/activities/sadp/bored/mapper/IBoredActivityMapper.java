package be.dpa.bootiful.activities.sadp.bored.mapper;

import be.dpa.bootiful.activities.dm.spi.ActivityRecord;
import be.dpa.bootiful.activities.sadp.bored.BoredActivityDTO;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.UUID;

@Mapper(componentModel = "spring")
public interface IBoredActivityMapper {

    @Mapping(source = "activity", target = "action")
    @Mapping(source = "key", target = "externalKey")
    @Mapping(source = "participants", target = "noOfParticipants")
    @Mapping(source = "link", target = "details")
    ActivityRecord toActivityRecord(BoredActivityDTO boredActivity);

    @AfterMapping
    default void appendAlternateKey(@MappingTarget ActivityRecord activityRecord) {
        activityRecord.setAlternateKey(UUID.randomUUID().toString());
    }
}
