package be.dpa.bootiful.activities.padp.rest;


import be.dpa.bootiful.activities.dm.api.Activity;
import be.dpa.bootiful.activities.dm.api.ActivityRequest;
import be.dpa.bootiful.activities.dm.api.IActivityService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ActivityController.class)
public class ActivityControllerTest {

    private static final String NEW_ACTIVITY_JSON = "/newActivity.json";

    private static final String NEW_ACTIVITY_INVALID_JSON = "/newActivityInvalid.json";
    private static final String UPDATE_ACTIVITY_JSON = "/updateActivity.json";
    private static final String UPDATE_ACTIVITY_INVALID_JSON = "/updateActivityInvalid.json";

    private static final String AK_STARE = "AKSTARE";
    private static final String ACTION_STARE_AT_THE_WALL = "Stare at the wall";

    private static final String AK_NETFLIX = "NETFLIX";
    private static final String ACTION_NETFLIX = "Netflix";

    private static final String URL_ACTIVITIES = "http://localhost/api/v1/activities/";

    private static final String AK_LEARN_HOW_THE_INTERNET_WORKS = "INTERNET";
    private static final String URL_ACTIVITY_INTERNET = "http://localhost/api/v1/activities/INTERNET";

    private static final String URL_ACTIVITY_BIKE = "http://localhost/api/v1/activities/BIKE";

    private static final String AK_BIKE = "BIKE";

    private Activity stareAtTheWallActivity;

    private Activity netflixActivity;

    @MockBean
    private IActivityService activityService;
    @Autowired
    private MockMvc mockMvc;

    private Activity createActivityResponse(String alternateKey, String action) {
        Activity activity = new Activity();
        activity.setAction(action);
        activity.setAlternateKey(alternateKey);
        activity.setNoOfParticipants(1);
        return activity;
    }

    @BeforeEach
    public void setUp() {
        stareAtTheWallActivity = createActivityResponse(AK_STARE, ACTION_STARE_AT_THE_WALL);
        netflixActivity = createActivityResponse(AK_NETFLIX, ACTION_NETFLIX);
    }

    @Test
    public void testGetActivitiesSuccess() throws Exception {
        Page<Activity> activityResponsePage = new PageImpl<>(Arrays.asList(stareAtTheWallActivity, netflixActivity), Pageable.ofSize(2), 2L);
        when(activityService.getActivities(anyInt(), anyInt())).thenReturn(Page.empty());
        mockMvc.perform(get("/api/v1/activities"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._links.self.href", is("http://localhost/api/v1/activities")))
                .andExpect(jsonPath("$.page.size", is(0)))
                .andExpect(jsonPath("$.page.totalElements", is(0)))
                .andExpect(jsonPath("$.page.totalPages", is(1)))
                .andExpect(jsonPath("$.page.number", is(0)));


        when(activityService.getActivities(anyInt(), anyInt())).thenReturn(activityResponsePage);
        mockMvc.perform(get("/api/v1/activities"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.activities[0].alternateKey", is(AK_STARE)))
                .andExpect(jsonPath("$._embedded.activities[0].action", is(ACTION_STARE_AT_THE_WALL)))
                .andExpect(jsonPath("$._embedded.activities[0]._links.self.href", is(URL_ACTIVITIES.concat(AK_STARE))))
                .andExpect(jsonPath("$._embedded.activities[1].alternateKey", is(AK_NETFLIX)))
                .andExpect(jsonPath("$._embedded.activities[1].action", is(ACTION_NETFLIX)))
                .andExpect(jsonPath("$._embedded.activities[1]._links.self.href", is(URL_ACTIVITIES.concat(AK_NETFLIX))));
    }

    @Test
    public void testGetNonExistentActivity() throws Exception {
        mockMvc.perform(get("/api/v1/activities/IDONTEXIST"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testGetActivity() throws Exception {
        when(activityService.getActivityBy(eq(AK_STARE))).thenReturn(Optional.of(stareAtTheWallActivity));
        mockMvc.perform(get("/api/v1/activities/".concat(AK_STARE)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.alternateKey", is(AK_STARE)))
                .andExpect(jsonPath("$.action", is(ACTION_STARE_AT_THE_WALL)));
    }

    private String readFile(String file) {
        try (InputStream inputStream = ActivityController.class.getResourceAsStream(file)) {
            byte[] bytes = inputStream.readAllBytes();
            return new String(bytes, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    @Test
    public void testNewActivity() throws Exception {
        Activity activity = new Activity();
        activity.setAlternateKey(AK_LEARN_HOW_THE_INTERNET_WORKS);
        when(activityService.newActivity(any(ActivityRequest.class))).thenReturn(activity);

        String newActivityJson = readFile(NEW_ACTIVITY_JSON);
        mockMvc.perform(post("/api/v1/activities")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newActivityJson)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(redirectedUrl(URL_ACTIVITY_INTERNET))
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
    public void testNewActivityBadRequest() throws Exception {
        String newActivityInvalidJson = readFile(NEW_ACTIVITY_INVALID_JSON);
        mockMvc.perform(post("/api/v1/activities")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newActivityInvalidJson)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testUpdateActivity() throws Exception {
        String updateActivityJson = readFile(UPDATE_ACTIVITY_JSON);
        mockMvc.perform(put("/api/v1/activities/BIKE")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateActivityJson)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(redirectedUrl(URL_ACTIVITY_BIKE))
                .andExpect(status().isNoContent());
        ArgumentCaptor<ActivityRequest> activityRequestArgumentCaptor = ArgumentCaptor.forClass(ActivityRequest.class);
        verify(activityService).updateActivity(eq(AK_BIKE), activityRequestArgumentCaptor.capture());
        ActivityRequest activityRequest = activityRequestArgumentCaptor.getValue();
        assertEquals("Go get your bike", activityRequest.getAction());
        assertEquals("outside", activityRequest.getType());
        assertEquals(1, activityRequest.getNoOfParticipants());
        assertEquals("", activityRequest.getDetails());
    }

    @Test
    public void testUpdateActivityBadRequest() throws Exception {
        String updateActivityInvalidJson = readFile(UPDATE_ACTIVITY_INVALID_JSON);
        mockMvc.perform(put("/api/v1/activities/BIKE")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateActivityInvalidJson)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testDeleteActivity() throws Exception {
        when(activityService.deleteActivity(eq(AK_NETFLIX))).thenReturn(Boolean.TRUE);
        mockMvc.perform(delete("/api/v1/activities/{alternateKey}", AK_NETFLIX))
                .andExpect(status().isNoContent());
        verify(activityService).deleteActivity(eq(AK_NETFLIX));
    }

    @Test
    public void testDeleteNonExistentActivity() throws Exception {
        when(activityService.deleteActivity(eq(AK_BIKE))).thenReturn(Boolean.FALSE);
        mockMvc.perform(delete("/api/v1/activities/{alternateKey}", AK_BIKE))
                .andExpect(status().isNotFound());
        verify(activityService).deleteActivity(eq(AK_BIKE));
    }
}
