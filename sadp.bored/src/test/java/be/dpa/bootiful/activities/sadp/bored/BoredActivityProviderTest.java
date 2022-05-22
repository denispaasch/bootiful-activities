package be.dpa.bootiful.activities.sadp.bored;

import be.dpa.bootiful.activities.dm.spi.ActivityRecord;
import be.dpa.bootiful.activities.dm.spi.IActivityImportRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.collections4.CollectionUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.event.annotation.BeforeTestClass;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = BoredTestConfig.class)
@TestPropertySource(locations = "classpath:application-test.properties")
public class BoredActivityProviderTest {

    private static final String IMPORT_ACTIVITY_JSON = "/importActivity.json";

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private BoredActivityProvider boredActivityProvider;

    @MockBean
    private IActivityImportRepository activityImportRepository;

    @MockBean
    private BoredApplicationReadyListener boredApplicationReadyListener;

    @Value("${activity.provider.url}")
    private String url;

    @Value("${activity.provider.fetch:10}")
    private int fetchAmount;

    private MockRestServiceServer mockServer;

    private String readFile(String file) {
        try (InputStream inputStream = BoredActivityProviderTest.class.getResourceAsStream(file)) {
            byte[] bytes = inputStream.readAllBytes();
            return new String(bytes, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    @BeforeEach
    public void init() {
        mockServer = MockRestServiceServer.createServer(restTemplate);
    }

    @Test
    public void testFetch() throws URISyntaxException {
        mockServer.expect(ExpectedCount.max(fetchAmount),
                        requestTo(new URI(url)))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(readFile(IMPORT_ACTIVITY_JSON))
                );
        boredActivityProvider.fetch();

        // Check if the REST calls have been issued
        mockServer.verify(Duration.ofSeconds(10L));

        // Capture its results
        ArgumentCaptor<ActivityRecord> activityRecordCaptor = ArgumentCaptor.forClass(ActivityRecord.class);
        verify(activityImportRepository, timeout(10000L).times(fetchAmount)).importActivity(activityRecordCaptor.capture());
        List<ActivityRecord> allActivityRecords = activityRecordCaptor.getAllValues();
        assertTrue(CollectionUtils.isNotEmpty(allActivityRecords));

        ActivityRecord activityRecord = allActivityRecords.get(0);
        assertEquals("relaxation", activityRecord.getType());
        assertEquals("Write a list of things you are grateful for", activityRecord.getAction());
        assertEquals(1, activityRecord.getNoOfParticipants());
        assertEquals("2062010", activityRecord.getExternalKey());

    }

}
