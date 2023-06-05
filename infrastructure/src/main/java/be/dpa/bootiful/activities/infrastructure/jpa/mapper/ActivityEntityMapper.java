package be.dpa.bootiful.activities.infrastructure.jpa.mapper;

import be.dpa.bootiful.activities.domain.spi.ActivityRecord;
import be.dpa.bootiful.activities.infrastructure.jpa.entities.ActivityEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Maps activity entities to records and vice versa.
 *
 * @author  denis
 */
@Mapper(componentModel = "spring")
public interface ActivityEntityMapper {

    @Mapping(target = "id", ignore = true)
    ActivityEntity toActivityEntity(ActivityRecord activity);

    ActivityRecord toActivityRecord(ActivityEntity activityEntity);
}
