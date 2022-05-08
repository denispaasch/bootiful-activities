package be.dpa.bootiful.activities;

import be.dpa.bootiful.activities.sadp.bored.BoredActivityProvider;
import be.dpa.bootiful.activities.sadp.jpa.ActivityEntity;
import be.dpa.bootiful.activities.sadp.jpa.IActivityEntityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
class BootifulActivitiesIT {

    private static final String ACTION_NOTHING = "Do nothing";

    private static final String TYPE_NOTHING = "nothing";

    private static final String URL_NOTHING = "https://www.giphy.com";

    private static final String ACTION_MORE_NOTHING = "Do more nothing";

    private static final String TYPE_MORE_NOTHING = "epic-nothing";

    private static final String URL_MORE_NOTHING = "https://www.sportbild.de";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private IActivityEntityRepository activityEntityRepository;

    @MockBean
    private BoredActivityProvider boredActivityProvider;

    private ActivityEntity createActivityEntity(String action, String type, String details) {
        ActivityEntity activityEntity = new ActivityEntity();
        String alternateKey = UUID.randomUUID().toString();
        activityEntity.setAlternateKey(alternateKey);
        activityEntity.setExternalKey(alternateKey);
        activityEntity.setAction(action);
        activityEntity.setType(type);
        activityEntity.setNoOfParticipants(1);
        activityEntity.setDetails(details);
        return activityEntity;
    }

    @BeforeEach
    public void setUp() {
        activityEntityRepository.save(createActivityEntity(ACTION_NOTHING, TYPE_NOTHING, URL_NOTHING));
        activityEntityRepository.save(createActivityEntity(ACTION_MORE_NOTHING, TYPE_MORE_NOTHING, URL_MORE_NOTHING));
    }

    @Test
    public void testGetActivities() throws Exception {
        mockMvc.perform(get("/api/v1/activities"))
                .andDo(print())
                .andExpect(status().isOk());
    }

}
