package be.dpa.bootiful.activities.padp.rest;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.hateoas.Link;

import java.net.MalformedURLException;
import java.net.URI;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ActivityRelationServiceTest {

    private static final String GOOD_LINK = "https://www.giphy.com";

    private static final String INVALID_LINK = "^-^";

    @Mock
    private Link link;

    @Test
    public void testConvertGoodLinkToUri() throws MalformedURLException {
        ActivityRelationService activityRelationService = new ActivityRelationService();
        when(link.getHref()).thenReturn(GOOD_LINK);
        Optional<URI> optUri = activityRelationService.convertToUri(link);
        URI uri = optUri.orElse(null);
        assertNotNull(uri);
        assertEquals(GOOD_LINK, uri.toURL().toString());
    }

    @Test
    public void testConvertInvalidLinkToUri() {
        ActivityRelationService activityRelationService = new ActivityRelationService();
        when(link.getHref()).thenReturn(INVALID_LINK);
        Optional<URI> optUri = activityRelationService.convertToUri(link);
        assertFalse(optUri.isPresent());
    }
}
