package be.dpa.bootiful.activities.domain.impl.mapper;

import be.dpa.bootiful.activities.domain.api.Activity;
import be.dpa.bootiful.activities.domain.api.ActivityRequest;
import be.dpa.bootiful.activities.domain.spi.ActivityRecord;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Maps activity records and requests from the secondary adapter to the primary adapter and vice versa.
 *
 * @author denis
 */
@Mapper(componentModel = "spring")
public interface ActivityMapper {

    Activity toActivityResponse(ActivityRecord activityRecord);

    @Mapping(target = "alternateKey", ignore = true)
    @Mapping(target = "externalKey", ignore = true)
    ActivityRecord toActivityRecord(ActivityRequest activityRequest);
}
