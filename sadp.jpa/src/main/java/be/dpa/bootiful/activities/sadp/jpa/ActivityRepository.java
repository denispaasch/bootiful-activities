package be.dpa.bootiful.activities.sadp.jpa;

import be.dpa.bootiful.activities.dm.spi.ActivityRecord;
import be.dpa.bootiful.activities.dm.spi.IActivityRepository;
import be.dpa.bootiful.activities.sadp.jpa.mapper.IActivityEntityMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * The activity repository implementation.
 *
 * @author denis
 */
@RequiredArgsConstructor
@Repository
public class ActivityRepository implements IActivityRepository {

    private final IActivityEntityMapper activityEntityMapper;

    private final IActivityEntityRepository activityEntityRepository;

    @Override
    public Page<ActivityRecord> getAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ActivityEntity> activityEntities = activityEntityRepository.findByOrderByTypeAscActionAsc(pageable);
        return activityEntities.map(activityEntityMapper::toActivityRecord);
    }

    @Override
    public Optional<ActivityRecord> getBy(String alternateKey) {
        Optional<ActivityEntity> optFound = activityEntityRepository.findByAlternateKey(alternateKey);
        return optFound.map(activityEntity -> activityEntityMapper.toActivityRecord(activityEntity));
    }

    private ActivityRecord doSave(ActivityEntity activityEntity) {
        return activityEntityMapper.toActivityRecord(activityEntityRepository.save(activityEntity));
    }

    @Override
    public ActivityRecord saveExternal(ActivityRecord activity) {
        ActivityEntity activityEntity = activityEntityMapper.toActivityEntity(activity);
        // Update if an activity with the key already exists
        Optional<ActivityEntity> optExists = activityEntityRepository.findByExternalKey(activity.getExternalKey());
        optExists.ifPresent(a -> activityEntity.setId(a.getId()));
        return doSave(activityEntity);
    }

    @Override
    public ActivityRecord save(ActivityRecord activity) {
        ActivityEntity activityEntity = activityEntityMapper.toActivityEntity(activity);
        Optional<ActivityEntity> optExists = activityEntityRepository.findByAlternateKey(activity.getAlternateKey());
        optExists.ifPresent(a -> activityEntity.setId(a.getId()));
        return doSave(activityEntity);
    }

    @Override
    public void delete(String alternateKey) {
        Optional<ActivityEntity> optActivityEntity = activityEntityRepository.findByAlternateKey(alternateKey);
        optActivityEntity.ifPresent(activityEntity -> activityEntityRepository.delete(activityEntity));
    }
}
