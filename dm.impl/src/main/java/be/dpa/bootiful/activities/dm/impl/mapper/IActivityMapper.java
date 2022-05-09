package be.dpa.bootiful.activities.dm.impl.mapper;

import be.dpa.bootiful.activities.dm.api.ActivityRequest;
import be.dpa.bootiful.activities.dm.api.ActivityModel;
import be.dpa.bootiful.activities.dm.spi.ActivityRecord;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Maps activity records and requests from the secondary adapter to the primary adapter and vice versa.
 *
 * @author denis
 */
@Mapper(componentModel = "spring")
public interface IActivityMapper {

    ActivityModel toActivityResponse(ActivityRecord activityRecord);

    @Mapping(target = "alternateKey", ignore = true)
    @Mapping(target = "externalKey", ignore = true)
    ActivityRecord toActivityRecord(ActivityRequest activityRequest);
}
