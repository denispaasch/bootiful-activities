package be.dpa.bootiful.activities.padp.rest;


import be.dpa.bootiful.activities.dm.api.ActivityRequest;
import be.dpa.bootiful.activities.dm.api.ActivityResponse;
import be.dpa.bootiful.activities.dm.api.IActivityService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.Link;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.junit.jupiter.api.Assertions.*;

@WebMvcTest(ActivityController.class)
public class ActivityControllerTest {

    private static final String NEW_ACTIVITY_JSON = "/newActivity.json";

    private static final String AK_STARE = "AKSTARE";
    private static final String ACTION_STARE_AT_THE_WALL = "Stare at the wall";
    private static final ActivityResponse STARE_AT_THE_WALL = createActivityResponse(AK_STARE, ACTION_STARE_AT_THE_WALL);

    private static final String AK_NETFLIX = "NETFLIX";
    private static final String ACTION_NETFLIX = "Netflix";

    private static final ActivityResponse NETFLIX = createActivityResponse(AK_NETFLIX, ACTION_NETFLIX);
    private static final String URL_ACTIVITIES = "http://localhost/api/v1/activities/";

    private static final String AK_LEARN_HOW_THE_INTERNET_WORKS = "INTERNET";
    public static final String URL_ACTIVITY_INTERNET = "http://localhost/api/v1/activities/INTERNET";

    private static ActivityResponse createActivityResponse(String alternateKey, String action) {
        ActivityResponse activityResponse = new ActivityResponse();
        activityResponse.setAction(action);
        activityResponse.setAlternateKey(alternateKey);
        activityResponse.setNoOfParticipants(1);
        return activityResponse;
    }

    @MockBean
    private IActivityService activityService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testGetActivitiesSuccess() throws Exception {
        mockMvc.perform(get("/api/v1/activities"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());

        when(activityService.getActivities()).thenReturn(Arrays.asList(STARE_AT_THE_WALL, NETFLIX));

        mockMvc.perform(get("/api/v1/activities"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].alternateKey", is(AK_STARE)))
                .andExpect(jsonPath("$[0].action", is(ACTION_STARE_AT_THE_WALL)))
                .andExpect(jsonPath("$[0].links[0].href", is(URL_ACTIVITIES.concat(AK_STARE))))
                .andExpect(jsonPath("$[1].alternateKey", is(AK_NETFLIX)))
                .andExpect(jsonPath("$[1].action", is(ACTION_NETFLIX)))
                .andExpect(jsonPath("$[1].links[0].href", is(URL_ACTIVITIES.concat(AK_NETFLIX))));
    }

    @Test
    public void testGetNonExistentActivity() throws Exception {
        mockMvc.perform(get("/api/v1/activities/IDONTEXIST"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testGetActivity() throws Exception {
        when(activityService.getActivityBy(eq(AK_STARE))).thenReturn(Optional.of(STARE_AT_THE_WALL));
        mockMvc.perform(get("/api/v1/activities/".concat(AK_STARE)))
                // .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.alternateKey", is(AK_STARE)))
                .andExpect(jsonPath("$.action", is(ACTION_STARE_AT_THE_WALL)));
    }

    private String readFile(String file) {
        try (InputStream inputStream = ActivityController.class.getResourceAsStream(NEW_ACTIVITY_JSON)) {
            return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    @Test
    public void testNewActivity() throws Exception {
        ActivityResponse activityResponse = new ActivityResponse();
        activityResponse.setAlternateKey(AK_LEARN_HOW_THE_INTERNET_WORKS);
        when(activityService.newActivity(any(ActivityRequest.class))).thenReturn(activityResponse);

        String newActivityJson = readFile(NEW_ACTIVITY_JSON);
        mockMvc.perform(post("/api/v1/activities")
                .contentType(MediaType.APPLICATION_JSON)
                .content(newActivityJson)
                .characterEncoding(StandardCharsets.UTF_8))
                // .andDo(print())
                .andExpect(status().isCreated());

        ArgumentCaptor<ActivityRequest> activityRequestArgumentCaptor = ArgumentCaptor.forClass(ActivityRequest.class);
        verify(activityService).newActivity(activityRequestArgumentCaptor.capture());
        ActivityRequest activityRequest = activityRequestArgumentCaptor.getValue();
        assertEquals("Learn how the internet works", activityRequest.getAction());
        assertEquals("education", activityRequest.getType());
        assertEquals(1, activityRequest.getNoOfParticipants());
        assertEquals("https://www.google.de", activityRequest.getDetails());
    }

    @Test
    public void testDeleteActivity() throws Exception {
        mockMvc.perform(delete("/api/v1/activities/{alternateKey}", AK_NETFLIX))
                .andExpect(status().isNoContent());
        verify(activityService).deleteActivity(eq(AK_NETFLIX));
    }
}
