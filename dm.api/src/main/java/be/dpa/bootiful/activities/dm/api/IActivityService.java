package be.dpa.bootiful.activities.dm.api;

import java.util.List;
import java.util.Optional;

public interface IActivityService {

    List<ActivityResponse> getActivities();

    Optional<ActivityResponse> getActivityBy(String alternateKey);

    ActivityResponse newActivity(ActivityRequest activityRequest);

    ActivityResponse updateActivity(String alternateKey, ActivityRequest activityRequest);

    void deleteActivity(String alternateKey);
}
