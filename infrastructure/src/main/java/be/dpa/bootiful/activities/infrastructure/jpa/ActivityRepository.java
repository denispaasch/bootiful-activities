package be.dpa.bootiful.activities.infrastructure.jpa;

import be.dpa.bootiful.activities.domain.spi.ActivityRecord;
import be.dpa.bootiful.activities.domain.spi.ParticipantRecord;
import be.dpa.bootiful.activities.infrastructure.jpa.entities.ActivityEntity;
import be.dpa.bootiful.activities.infrastructure.jpa.entities.ActivityParticipantEntity;
import be.dpa.bootiful.activities.infrastructure.jpa.entities.ParticipantEntity;
import be.dpa.bootiful.activities.infrastructure.jpa.filter.CustomRsqlVisitor;
import be.dpa.bootiful.activities.infrastructure.jpa.mapper.ActivityEntityMapper;
import be.dpa.bootiful.activities.infrastructure.jpa.mapper.ParticipantEntityMapper;
import cz.jirutka.rsql.parser.RSQLParser;
import cz.jirutka.rsql.parser.ast.Node;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * The activity repository implementation.
 *
 * @author denis
 */
@RequiredArgsConstructor
@Repository
public class ActivityRepository implements be.dpa.bootiful.activities.domain.spi.ActivityRepository {

    private static final long ZERO_ROWS_AFFECTED = 0L;
    private final ActivityEntityMapper activityEntityMapper;

    private final ParticipantEntityMapper participantEntityMapper;

    private final ActivityEntityRepository activityEntityRepository;

    private final ActivityParticipantEntityRepository activityParticipantEntityRepository;

    private final ActivityParticipantRepository activityParticipantRepository;

    private final ParticipantEntityRepository participantEntityRepository;

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

    private Page<ParticipantRecord> getParticipantsBy(String alternateKey, Pageable pageable) {
        Page<ActivityParticipantEntity> activityParticipants =
                activityParticipantEntityRepository
                        .findActivityParticipants(alternateKey, pageable);
        return activityParticipants.map(activityParticipantEntity ->
                participantEntityMapper.toParticipantRecord(activityParticipantEntity.getParticipant()));
    }

    @Override
    public Page<ParticipantRecord> getParticipantsBy(String alternateKey, int page, int size) {
        return getParticipantsBy(alternateKey, PageRequest.of(page, size));
    }

    @Override
    public List<ParticipantRecord> getParticipantsBy(String alternateKey) {
        Page<ParticipantRecord> allParticipants = getParticipantsBy(alternateKey, Pageable.unpaged());
        return allParticipants.getContent();
    }

    private ParticipantEntity newParticipant(ParticipantRecord participantRecord) {
        ParticipantEntity participantEntity = participantEntityMapper.toParticipantEntity(participantRecord);
        participantEntity.setAlternateKey(UUID.randomUUID().toString());
        return participantEntityRepository.save(participantEntity);
    }

    @Override
    public ParticipantRecord newParticipant(String alternateKey, ParticipantRecord participantRecord) {
        ActivityEntity activityEntity = activityEntityRepository.findByAlternateKey(alternateKey).get();
        ParticipantEntity participantEntity = newParticipant(participantRecord);
        ActivityParticipantEntity assignment =
                activityParticipantRepository.createAssignment(activityEntity, participantEntity);
        activityParticipantRepository.save(assignment);
        return participantEntityMapper.toParticipantRecord(participantEntity);
    }

    private ActivityRecord doSave(ActivityEntity activityEntity) {
        return activityEntityMapper.toActivityRecord(activityEntityRepository.save(activityEntity));
    }

    @Override
    public ActivityRecord save(ActivityRecord activity) {
        ActivityEntity activityEntity = activityEntityMapper.toActivityEntity(activity);
        Optional<ActivityEntity> optExists = activityEntityRepository.findByAlternateKey(activity.alternateKey());
        optExists.ifPresent(a -> activityEntity.setId(a.getId()));
        return doSave(activityEntity);
    }

    @Override
    public long delete(String alternateKey) {
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
