package be.dpa.bootiful.activities;

import be.dpa.bootiful.activities.dm.spi.ActivityRecord;
import be.dpa.bootiful.activities.dm.spi.IActivityImportRepository;
import be.dpa.bootiful.activities.sadp.bored.BoredActivityProvider;
import be.dpa.bootiful.activities.sadp.jpa.IActivityEntityRepository;
import be.dpa.bootiful.activities.sadp.jpa.IActivityParticipantEntityRepository;
import be.dpa.bootiful.activities.sadp.jpa.IParticipantEntityRepository;
import be.dpa.bootiful.activities.sadp.jpa.entities.ActivityEntity;
import be.dpa.bootiful.activities.sadp.jpa.entities.ParticipantEntity;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.IteratorUtils;
import org.apache.commons.lang3.StringUtils;
import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;
import org.jeasy.random.FieldPredicates;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
class BootifulActivitiesIT {

    private static final String NEW_ACTIVITY_JSON = "/newActivity.json";

    private static final String UPDATE_ACTIVITY_JSON = "/updateActivity.json";

    private static final int ACTIVITY_COUNT = 100;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private IActivityEntityRepository activityEntityRepository;

    @Autowired
    private IParticipantEntityRepository participantEntityRepository;

    @Autowired
    private IActivityParticipantEntityRepository activityParticipantEntityRepository;

    @Autowired
    private IActivityImportRepository activityImportRepository;

    @MockBean
    private BoredActivityProvider boredActivityProvider;

    private String readFile(String file) {
        try (InputStream inputStream = BootifulActivitiesIT.class.getResourceAsStream(file)) {
            byte[] bytes = inputStream.readAllBytes();
            return new String(bytes, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    @BeforeEach
    public void setUp() {
        activityParticipantEntityRepository.deleteAll();
        activityEntityRepository.deleteAll();
        participantEntityRepository.deleteAll();
    }

    @Test
    public void testGetActivities() throws Exception {
        EasyRandomParameters parameters = new EasyRandomParameters()
                .excludeField(FieldPredicates.named("id").and(FieldPredicates.inClass(ActivityEntity.class)))
                .excludeField(FieldPredicates.named("participantAssignments").and(FieldPredicates.inClass(ActivityEntity.class)))
                .randomize(f -> StringUtils.equals(f.getName(), "alternateKey"), new AlternateKeyRandomizer())
                .randomize(Integer.class, new NoOfParticipantsRandomizer(1, 10));
        EasyRandom generator = new EasyRandom(parameters);
        List<ActivityEntity> activityEntities =
                generator.objects(ActivityEntity.class, ACTIVITY_COUNT).collect(Collectors.toList());
        activityEntityRepository.saveAll(activityEntities);
        mockMvc.perform(get("/api/v1/activities"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.page.size", is(5)))
                .andExpect(jsonPath("$.page.totalElements", is(100)))
                .andExpect(jsonPath("$.page.totalPages", is(20)))
                .andExpect(jsonPath("$.page.number", is(0)));
        mockMvc.perform(get("/api/v1/activities?page=1&size=50"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.page.size", is(50)))
                .andExpect(jsonPath("$.page.totalElements", is(100)))
                .andExpect(jsonPath("$.page.totalPages", is(2)))
                .andExpect(jsonPath("$.page.number", is(1)));
    }


    private void assertActivityEntity(ActivityEntity activityEntity,
                                      String action, String type, int noOfParticipants, String details) {
        assertNotNull(activityEntity.getId());
        assertNotNull(activityEntity.getAlternateKey());
        assertNull(activityEntity.getExternalKey());
        assertEquals(action, activityEntity.getAction());
        assertEquals(type, activityEntity.getType());
        assertEquals(noOfParticipants, activityEntity.getNoOfParticipants());
        assertEquals(details, activityEntity.getDetails());
    }

    @Test
    public void testNewActivity() throws Exception {
        String newActivityJson = readFile(NEW_ACTIVITY_JSON);
        mockMvc.perform(post("/api/v1/activities")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(newActivityJson))
                .andExpect(status().isCreated());

        // Check if it has been actually saved within the database
        Iterable<ActivityEntity> activityEntityIterable = activityEntityRepository.findAll();
        List<ActivityEntity> activityEntities = IteratorUtils.toList(activityEntityIterable.iterator());
        assertEquals(1, activityEntities.size());
        assertActivityEntity(activityEntities.get(0), "Change the diaper of my daughter.. again",
                "infrastructure", 2, "https://www.verywellfamily.com/how-to-change-a-diaper-289239");
    }

    @Test
    public void testInvalidNewActivity() throws Exception {
        mockMvc.perform(post("/api/v1/activities")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isBadRequest());
    }


    private ActivityEntity createRandomActivity() {
        EasyRandomParameters parameters = new EasyRandomParameters()
                .excludeField(FieldPredicates.named("id").and(FieldPredicates.inClass(ActivityEntity.class)))
                .randomize(f -> StringUtils.equals(f.getName(), "alternateKey"), new AlternateKeyRandomizer())
                .randomize(Integer.class, new NoOfParticipantsRandomizer(1, 10));
        EasyRandom generator = new EasyRandom(parameters);
        return activityEntityRepository.save(generator.nextObject(ActivityEntity.class));
    }

    @Test
    public void testUpdateActivity() throws Exception {
        ActivityEntity activityEntity = createRandomActivity();
        String updateActivityJson = readFile(UPDATE_ACTIVITY_JSON);
        String updateActivityUrl = "/api/v1/activities/".concat(activityEntity.getAlternateKey());
        mockMvc.perform(put(updateActivityUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(updateActivityJson))
                .andExpect(status().isNoContent())
                .andExpect(redirectedUrl("http://localhost".concat(updateActivityUrl)));

        Optional<ActivityEntity> updatedActivityEntity =
                activityEntityRepository.findByAlternateKey(activityEntity.getAlternateKey());
        assertTrue(updatedActivityEntity.isPresent());
        assertActivityEntity(updatedActivityEntity.get(), "Public facebook party",
                "party", 1500, "https://newsfeed.time.com/2011/06/06/facebook-flub-german-teen-gets-1500-unwanted-guests-after-party-invite-goes-viral");
    }

    @Test
    public void testImportActivities() throws Exception {
        EasyRandomParameters parameters = new EasyRandomParameters()
                .randomize(f -> StringUtils.equals(f.getName(), "alternateKey"), new AlternateKeyRandomizer())
                .randomize(f -> StringUtils.equals(f.getName(), "externalKey"), new AlternateKeyRandomizer())
                .randomize(Integer.class, new NoOfParticipantsRandomizer(1, 10));
        EasyRandom generator = new EasyRandom(parameters);
        List<ActivityRecord> activityRecords =
                generator.objects(ActivityRecord.class, 5).collect(Collectors.toList());
        Integer participantSum =
                activityRecords.stream().map(ActivityRecord::getNoOfParticipants)
                        .collect(Collectors.summingInt(Integer::intValue));
        activityRecords.forEach(activityImportRepository::importActivity);

        // Check if the activities have been persisted
        Iterable<ActivityEntity> activityEntityIterable = activityEntityRepository.findAll();
        List<ActivityEntity> activityEntities = IteratorUtils.toList(activityEntityIterable.iterator());
        assertEquals(5, activityEntities.size());

        // Ensure that also participants have been generated by the importer
        Iterable<ParticipantEntity> participantEntityIterable = participantEntityRepository.findAll();
        List<ParticipantEntity> participantEntities = IteratorUtils.toList(participantEntityIterable.iterator());
        assertEquals(participantSum, participantEntities.size());

        // Check if the activities are available using the primary adapter
        mockMvc.perform(get("/api/v1/activities"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.page.size", is(5)))
                .andExpect(jsonPath("$.page.totalElements", is(5)))
                .andExpect(jsonPath("$.page.totalPages", is(1)))
                .andExpect(jsonPath("$.page.number", is(0)));
    }

    @Test
    public void testDeleteActivity() throws Exception {
        ActivityEntity activityEntity = createRandomActivity();
        mockMvc.perform(delete("/api/v1/activities/{alternateKey}", activityEntity.getAlternateKey()))
                .andExpect(status().isNoContent());
        Iterable<ActivityEntity> activityEntityIterable = activityEntityRepository.findAll();
        List<ActivityEntity> activityEntities = IteratorUtils.toList(activityEntityIterable.iterator());
        assertTrue(CollectionUtils.isEmpty(activityEntities));
    }
}
