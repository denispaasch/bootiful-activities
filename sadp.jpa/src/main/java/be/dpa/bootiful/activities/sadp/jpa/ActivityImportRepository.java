package be.dpa.bootiful.activities.sadp.jpa;

import be.dpa.bootiful.activities.dm.spi.ActivityRecord;
import be.dpa.bootiful.activities.dm.spi.IActivityImportRepository;
import be.dpa.bootiful.activities.sadp.jpa.entities.ActivityEntity;
import be.dpa.bootiful.activities.sadp.jpa.entities.ActivityParticipantEntity;
import be.dpa.bootiful.activities.sadp.jpa.entities.ParticipantEntity;
import be.dpa.bootiful.activities.sadp.jpa.mapper.IActivityEntityMapper;
import com.github.javafaker.Faker;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.IteratorUtils;
import org.springframework.stereotype.Repository;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Activity import repository.
 *
 * @author denis
 */
@Repository
@RequiredArgsConstructor
public class ActivityImportRepository implements IActivityImportRepository {

    private final Faker faker = new Faker(Locale.ENGLISH);

    private final IActivityEntityMapper activityEntityMapper;

    private final IActivityEntityRepository activityEntityRepository;

    private final IParticipantEntityRepository participantRepository;

    private final IActivityParticipantEntityRepository activityParticipantEntityRepository;

    private Set<ParticipantEntity> randomParticipants(Integer noOfParticipants) {
        Set<ParticipantEntity> participantEntities = new LinkedHashSet<>(noOfParticipants);
        for (int i = 0; i < noOfParticipants; ++i) {
            ParticipantEntity participantEntity = new ParticipantEntity();
            participantEntity.setAlternateKey(UUID.randomUUID().toString());
            participantEntity.setFirstName(faker.cat().name());
            participantEntity.setLastName(faker.artist().name());
            participantEntities.add(participantEntity);
        }
        return participantEntities;
    }

    private ActivityParticipantEntity createAssignment(ActivityEntity activityEntity,
                                                       ParticipantEntity participantEntity) {
        ActivityParticipantEntity activityParticipantEntity = new ActivityParticipantEntity();
        activityParticipantEntity.setActivity(activityEntity);
        activityParticipantEntity.setParticipant(participantEntity);
        return activityParticipantEntity;
    }

    private void createParticipants(ActivityEntity activityEntity) {
        Integer noOfParticipants = activityEntity.getNoOfParticipants();
        if (noOfParticipants > 0) {
            Iterable<ParticipantEntity> participantEntityIterable =
                    participantRepository.saveAll(randomParticipants(noOfParticipants));
            List<ParticipantEntity> participantEntities = IteratorUtils.toList(participantEntityIterable.iterator());
            List<ActivityParticipantEntity> assignments = participantEntities.stream().map(participantEntity ->
                    createAssignment(activityEntity, participantEntity)).collect(Collectors.toList());
            activityParticipantEntityRepository.saveAll(assignments);
        }
    }

    @Override
    public void importActivity(ActivityRecord activityRecord) {
        ActivityEntity activityEntity = activityEntityMapper.toActivityEntity(activityRecord);
        Optional<ActivityEntity> optExists = activityEntityRepository
                .findByExternalKey(activityRecord.getExternalKey());
        if (!optExists.isPresent()) {
            createParticipants(activityEntityRepository.save(activityEntity));
        }
    }
}
