package be.dpa.bootiful.activities.sadp.jpa;

import be.dpa.bootiful.activities.dm.spi.ActivityRecord;
import be.dpa.bootiful.activities.dm.spi.IActivityRepository;
import be.dpa.bootiful.activities.sadp.jpa.entities.ActivityEntity;
import be.dpa.bootiful.activities.sadp.jpa.entities.ActivityParticipantEntity;
import be.dpa.bootiful.activities.sadp.jpa.entities.ParticipantEntity;
import be.dpa.bootiful.activities.sadp.jpa.mapper.IActivityEntityMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * The activity repository implementation.
 *
 * @author denis
 */
@RequiredArgsConstructor
@Repository
public class ActivityRepository implements IActivityRepository {

    private static final long ZERO_ROWS_AFFECTED = 0L;
    private final IActivityEntityMapper activityEntityMapper;

    private final IActivityEntityRepository activityEntityRepository;

    private final IActivityParticipantEntityRepository activityParticipantEntityRepository;

    private final IParticipantEntityRepository participantEntityRepository;

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
    public ActivityRecord save(ActivityRecord activity) {
        ActivityEntity activityEntity = activityEntityMapper.toActivityEntity(activity);
        Optional<ActivityEntity> optExists = activityEntityRepository.findByAlternateKey(activity.getAlternateKey());
        optExists.ifPresent(a -> activityEntity.setId(a.getId()));
        return doSave(activityEntity);
    }

    @Override
    public Long delete(String alternateKey) {
        Optional<ActivityEntity> optActivityEntity = activityEntityRepository.findByAlternateKey(alternateKey);
        if (!optActivityEntity.isPresent()) {
            return ZERO_ROWS_AFFECTED;
        }

        ActivityEntity activityEntity = optActivityEntity.get();
        Set<ActivityParticipantEntity> activityParticipantEntities = activityEntity.getParticipantAssignments();
        Set<ParticipantEntity> participantEntities = activityParticipantEntities.stream()
                .map(ActivityParticipantEntity::getParticipant).collect(Collectors.toSet());
        activityParticipantEntityRepository.deleteAll(activityParticipantEntities);
        participantEntityRepository.deleteAll(participantEntities);
        return activityEntityRepository.deleteByAlternateKey(alternateKey);
    }
}
