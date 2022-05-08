package be.dpa.bootiful.activities.dm.impl.mapper;

import be.dpa.bootiful.activities.dm.api.ActivityRequest;
import be.dpa.bootiful.activities.dm.api.ActivityResponse;
import be.dpa.bootiful.activities.dm.spi.ActivityRecord;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ActivityMapper {

    ActivityResponse toActivityResponse(ActivityRecord activityRecord);

    List<ActivityResponse> toActivityResponses(List<ActivityRecord> activityRecords);

    @Mapping(target = "alternateKey", ignore = true)
    @Mapping(target = "externalKey", ignore = true)
    ActivityRecord toActivityRecord(ActivityRequest activityRequest);
}
