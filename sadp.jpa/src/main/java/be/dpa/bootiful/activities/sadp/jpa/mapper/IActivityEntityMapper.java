package be.dpa.bootiful.activities.sadp.jpa.mapper;

import be.dpa.bootiful.activities.dm.spi.ActivityRecord;
import be.dpa.bootiful.activities.sadp.jpa.entities.ActivityEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

/**
 * Maps activity entities to records and vice versa.
 *
 * @author  denis
 */
@Mapper(componentModel = "spring")
public interface IActivityEntityMapper {

    @Mapping(target = "id", ignore = true)
    ActivityEntity toActivityEntity(ActivityRecord activity);

    ActivityRecord toActivityRecord(ActivityEntity activityEntity);

    List<ActivityRecord> toActivityRecords(Iterable<ActivityEntity> activityEntities);
}
