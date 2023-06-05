package be.dpa.bootiful.activities.infrastructure.jpa;

import be.dpa.bootiful.activities.domain.spi.ActivityRecord;
import be.dpa.bootiful.activities.infrastructure.jpa.entities.ActivityEntity;
import be.dpa.bootiful.activities.infrastructure.jpa.entities.ActivityParticipantEntity;
import be.dpa.bootiful.activities.infrastructure.jpa.entities.ParticipantEntity;
import be.dpa.bootiful.activities.infrastructure.jpa.mapper.ActivityEntityMapper;
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
public class ActivityImportRepository implements be.dpa.bootiful.activities.domain.spi.ActivityImportRepository {

    private final Faker faker = new Faker(Locale.ENGLISH);

    private final ActivityEntityMapper activityEntityMapper;

    private final ActivityEntityRepository activityEntityRepository;

    private final ParticipantEntityRepository participantRepository;

    private final ActivityParticipantRepository activityParticipantRepository;

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



    private void createParticipants(ActivityEntity activityEntity) {
        Integer noOfParticipants = activityEntity.getNoOfParticipants();
        if (noOfParticipants > 0) {
            Iterable<ParticipantEntity> participantEntityIterable =
                    participantRepository.saveAll(randomParticipants(noOfParticipants));
            List<ParticipantEntity> participantEntities = IteratorUtils.toList(participantEntityIterable.iterator());
            List<ActivityParticipantEntity> assignments = participantEntities.stream().map(participantEntity ->
                    activityParticipantRepository.createAssignment(activityEntity, participantEntity))
                    .collect(Collectors.toList());
            activityParticipantRepository.saveAll(assignments);
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
