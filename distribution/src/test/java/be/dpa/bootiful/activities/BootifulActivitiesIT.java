package be.dpa.bootiful.activities;

import be.dpa.bootiful.activities.dm.spi.ActivityRecord;
import be.dpa.bootiful.activities.dm.spi.IActivityImportRepository;
import be.dpa.bootiful.activities.sadp.bored.BoredActivityProvider;
import be.dpa.bootiful.activities.sadp.jpa.IActivityEntityRepository;
import be.dpa.bootiful.activities.sadp.jpa.IParticipantEntityRepository;
import be.dpa.bootiful.activities.sadp.jpa.entities.ActivityEntity;
import be.dpa.bootiful.activities.sadp.jpa.entities.ParticipantEntity;
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
import java.util.UUID;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
class BootifulActivitiesIT {

    private static final String NEW_ACTIVITY_JSON = "/newActivity.json";

    private static final int ACTIVITY_COUNT = 100;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private IActivityEntityRepository activityEntityRepository;

    @Autowired
    private IParticipantEntityRepository participantEntityRepository;

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
        activityEntityRepository.deleteAll();
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
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.page.size", is(50)))
                .andExpect(jsonPath("$.page.totalElements", is(100)))
                .andExpect(jsonPath("$.page.totalPages", is(2)))
                .andExpect(jsonPath("$.page.number", is(1)));
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
        ActivityEntity activityEntity = activityEntities.get(0);
        assertNotNull(activityEntity.getId());
        assertNotNull(activityEntity.getAlternateKey());
        assertNull(activityEntity.getExternalKey());
        assertEquals("Change the diaper of my daughter.. again", activityEntity.getAction());
        assertEquals("infrastructure", activityEntity.getType());
        assertEquals(2, activityEntity.getNoOfParticipants());
        assertEquals("https://www.verywellfamily.com/how-to-change-a-diaper-289239", activityEntity.getDetails());
    }

    @Test
    public void testInvalidNewActivity() throws Exception {
        mockMvc.perform(post("/api/v1/activities")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testImportActivities() {
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

        Iterable<ActivityEntity> activityEntityIterable = activityEntityRepository.findAll();
        List<ActivityEntity> activityEntities = IteratorUtils.toList(activityEntityIterable.iterator());
        assertEquals(5, activityEntities.size());

        Iterable<ParticipantEntity> participantEntityIterable = participantEntityRepository.findAll();
        List<ParticipantEntity> participantEntities = IteratorUtils.toList(participantEntityIterable.iterator());
        assertEquals(participantSum, participantEntities.size());
    }
}
