package be.dpa.bootiful.activities;

import be.dpa.bootiful.activities.sadp.bored.BoredActivityProvider;
import be.dpa.bootiful.activities.sadp.jpa.ActivityEntity;
import be.dpa.bootiful.activities.sadp.jpa.IActivityEntityRepository;
import org.apache.commons.collections4.IteratorUtils;
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
import java.util.stream.IntStream;

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

    private static final String ACTION_NOTHING = "Do nothing";

    private static final String TYPE_NOTHING = "nothing";

    private static final String URL_NOTHING = "https://www.giphy.com";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private IActivityEntityRepository activityEntityRepository;

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


    private ActivityEntity createActivityEntity(int index) {
        ActivityEntity activityEntity = new ActivityEntity();
        String alternateKey = UUID.randomUUID().toString();
        activityEntity.setAlternateKey(alternateKey);
        activityEntity.setExternalKey(alternateKey);

        String suffix = String.valueOf(index);
        activityEntity.setAction(ACTION_NOTHING.concat(suffix));
        activityEntity.setType(TYPE_NOTHING.concat(suffix));
        activityEntity.setNoOfParticipants(1);
        activityEntity.setDetails(URL_NOTHING);
        return activityEntity;
    }

    @BeforeEach
    public void setUp() {
        activityEntityRepository.deleteAll();
    }

    @Test
    public void testGetActivities() throws Exception {
        List<ActivityEntity> activityEntities =
                IntStream.range(0, ACTIVITY_COUNT).mapToObj(this::createActivityEntity).collect(Collectors.toList());
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
                .andDo(print());
    }

}
