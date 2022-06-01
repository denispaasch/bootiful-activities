package be.dpa.bootiful.activities.sadp.jpa;

import be.dpa.bootiful.activities.dm.spi.ActivityRecord;
import be.dpa.bootiful.activities.dm.spi.IActivityRepository;
import be.dpa.bootiful.activities.dm.spi.ParticipantRecord;
import be.dpa.bootiful.activities.sadp.jpa.entities.ActivityEntity;
import be.dpa.bootiful.activities.sadp.jpa.entities.ActivityParticipantEntity;
import be.dpa.bootiful.activities.sadp.jpa.entities.ParticipantEntity;
import be.dpa.bootiful.activities.sadp.jpa.filter.CustomRsqlVisitor;
import be.dpa.bootiful.activities.sadp.jpa.mapper.IActivityEntityMapper;
import be.dpa.bootiful.activities.sadp.jpa.mapper.IParticipantEntityMapper;
import cz.jirutka.rsql.parser.RSQLParser;
import cz.jirutka.rsql.parser.ast.Node;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
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

    private final IParticipantEntityMapper participantEntityMapper;

    private final IActivityEntityRepository activityEntityRepository;

    private final IActivityParticipantEntityRepository activityParticipantEntityRepository;

    private final IParticipantEntityRepository participantEntityRepository;

    private Page<ActivityEntity> doGetAll(Optional<String> search, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        if (search.isPresent()) {
            Node rootNode = new RSQLParser().parse(search.get());
            Specification<ActivityEntity> specification = rootNode.accept(new CustomRsqlVisitor<>());
            // Sort.by((Sort.Direction.ASC, "type", "action");
            return activityEntityRepository.findAll(specification, pageable);
        }
        return activityEntityRepository.findByOrderByTypeAscActionAsc(pageable);
    }

    @Override
    public Page<ActivityRecord> getAll(Optional<String> search, int page, int size) {
        Page<ActivityEntity> activityEntities = doGetAll(search, page, size);
        return activityEntities.map(activityEntityMapper::toActivityRecord);
    }

    @Override
    public Optional<ActivityRecord> getBy(String alternateKey) {
        Optional<ActivityEntity> optFound = activityEntityRepository.findByAlternateKey(alternateKey);
        return optFound.map(activityEntity -> activityEntityMapper.toActivityRecord(activityEntity));
    }

    @Override
    public Page<ParticipantRecord> getParticipantsBy(String alternateKey, int page, int size) {
        Page<ActivityParticipantEntity> activityParticipants =
                activityParticipantEntityRepository
                        .findActivityParticipants(alternateKey, PageRequest.of(page, size));
        return activityParticipants.map(activityParticipantEntity ->
                participantEntityMapper.toParticipantRecord(activityParticipantEntity.getParticipant()));
    }

    @Override
    public ParticipantRecord assignParticipant(String alternateKey, ParticipantRecord participantRecord) {

        // TODO check if the participant already exists
        // assign it to the passed activity

        return null;
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
