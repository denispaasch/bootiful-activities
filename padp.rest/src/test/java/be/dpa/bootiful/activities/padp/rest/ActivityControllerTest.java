package be.dpa.bootiful.activities.padp.rest;


import be.dpa.bootiful.activities.dm.api.Activity;
import be.dpa.bootiful.activities.dm.api.ActivityRequest;
import be.dpa.bootiful.activities.dm.api.IActivityService;
import be.dpa.bootiful.activities.dm.api.Participant;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.Link;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.doReturn;
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
    private static final String TYPE_SAD = "sad";
    private static final String AK_NETFLIX = "NETFLIX";
    private static final String ACTION_NETFLIX = "Netflix";
    private static final String TYPE_SOFA = "sofa";
    private static final String DETAILS_NETFLIX = "https://www.netflix.com";
    private static final String URL_ACTIVITIES = "http://localhost/api/v1/activities/";
    private static final String AK_LEARN_HOW_THE_INTERNET_WORKS = "INTERNET";
    private static final String URL_ACTIVITY_INTERNET = "http://localhost/api/v1/activities/INTERNET";
    private static final String URL_ACTIVITY_BIKE = "http://localhost/api/v1/activities/BIKE";
    private static final String AK_BIKE = "BIKE";
    private static final String AK_TOM_BOLA = "TOMBOLA";
    private static final String TOM = "Tom";
    private static final String BOLA = "Bola";

    private Activity stareAtTheWallActivity;

    private Activity netflixActivity;

    private Participant tomBolaParticipant;

    @MockBean
    private IActivityService activityService;

    @SpyBean
    private ActivityRelationService activityRelationService;

    @Autowired
    private MockMvc mockMvc;

    private Activity createActivity(String alternateKey, String action, String type, String details) {
        Activity activity = new Activity();
        activity.setAction(action);
        activity.setAlternateKey(alternateKey);
        activity.setType(type);
        activity.setDetails(details);
        activity.setNoOfParticipants(1);
        return activity;
    }

    private Participant createParticipant(String alternateKey, String firstName, String lastName) {
        Participant participant = new Participant();
        participant.setAlternateKey(alternateKey);
        participant.setFirstName(firstName);
        participant.setLastName(lastName);
        return participant;
    }

    @BeforeEach
    public void setUp() {
        stareAtTheWallActivity = createActivity(AK_STARE, ACTION_STARE_AT_THE_WALL, TYPE_SAD, StringUtils.EMPTY);
        netflixActivity = createActivity(AK_NETFLIX, ACTION_NETFLIX, TYPE_SOFA, DETAILS_NETFLIX);
        tomBolaParticipant = createParticipant(AK_TOM_BOLA, TOM, BOLA);
    }

    @Test
    public void testGetNoActivities() throws Exception {
        when(activityService.getActivities(any(Optional.class), anyInt(), anyInt())).thenReturn(Page.empty());
        mockMvc.perform(get("/api/v1/activities"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testGetActivities() throws Exception {
        Page<Activity> activityPage = new PageImpl<>(Arrays.asList(stareAtTheWallActivity, netflixActivity), Pageable.ofSize(2), 2L);
        when(activityService.getActivities(any(Optional.class), anyInt(), anyInt())).thenReturn(activityPage);
        mockMvc.perform(get("/api/v1/activities"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.activities[0].alternateKey", is(AK_STARE)))
                .andExpect(jsonPath("$._embedded.activities[0].action", is(ACTION_STARE_AT_THE_WALL)))
                .andExpect(jsonPath("$._embedded.activities[0].type", is(TYPE_SAD)))
                .andExpect(jsonPath("$._embedded.activities[0].details", is(StringUtils.EMPTY)))
                .andExpect(jsonPath("$._embedded.activities[0]._links.self.href", is(URL_ACTIVITIES.concat(AK_STARE))))
                .andExpect(jsonPath("$._embedded.activities[1].alternateKey", is(AK_NETFLIX)))
                .andExpect(jsonPath("$._embedded.activities[1].action", is(ACTION_NETFLIX)))
                .andExpect(jsonPath("$._embedded.activities[1].type", is(TYPE_SOFA)))
                .andExpect(jsonPath("$._embedded.activities[1].details", is(DETAILS_NETFLIX)))
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

    @Test
    public void testGetNoActivityParticipants() throws Exception {
        when(activityService.getActivityParticipants(eq(AK_BIKE), eq(0), eq(5)))
                .thenReturn(Page.empty());
        mockMvc.perform(get("/api/v1/activities/".concat(AK_BIKE).concat("/participants")))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testGetActivityParticipants() throws Exception {
        when(activityService.getActivityParticipants(eq(AK_BIKE), eq(0), eq(5)))
                .thenReturn(new PageImpl<>(Arrays.asList(tomBolaParticipant), Pageable.ofSize(1), 1L));
        mockMvc.perform(get("/api/v1/activities/".concat(AK_BIKE).concat("/participants")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.participants[0].alternateKey", is(AK_TOM_BOLA)))
                .andExpect(jsonPath("$._embedded.participants[0].firstName", is(TOM)))
                .andExpect(jsonPath("$._embedded.participants[0].lastName", is(BOLA)));

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
    public void testNewActivityInvalidRequest() throws Exception {
        String newActivityInvalidJson = readFile(NEW_ACTIVITY_INVALID_JSON);
        mockMvc.perform(post("/api/v1/activities")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newActivityInvalidJson)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testNewActivityInvalidLink() throws Exception {
        Activity activity = new Activity();
        activity.setAlternateKey(AK_LEARN_HOW_THE_INTERNET_WORKS);
        when(activityService.newActivity(any(ActivityRequest.class))).thenReturn(activity);
        doReturn(Optional.empty()).when(activityRelationService).convertToUri(any(Link.class));
        String newActivityJson = readFile(NEW_ACTIVITY_JSON);
        MvcResult mvcResult = mockMvc.perform(post("/api/v1/activities")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newActivityJson)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isBadRequest())
                .andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        assertTrue(StringUtils.isNotEmpty(content));
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
    public void testUpdateActivityInvalidRequest() throws Exception {
        String updateActivityInvalidJson = readFile(UPDATE_ACTIVITY_INVALID_JSON);
        mockMvc.perform(put("/api/v1/activities/BIKE")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateActivityInvalidJson)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isBadRequest());

    }

    @Test
    public void testUpdateActivityInvalidLink() throws Exception {
        doReturn(Optional.empty()).when(activityRelationService).convertToUri(any(Link.class));
        String updateActivityJson = readFile(UPDATE_ACTIVITY_JSON);
        MvcResult mvcResult = mockMvc.perform(put("/api/v1/activities/BIKE")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateActivityJson)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isBadRequest())
                .andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        assertTrue(StringUtils.isNotEmpty(content));
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
