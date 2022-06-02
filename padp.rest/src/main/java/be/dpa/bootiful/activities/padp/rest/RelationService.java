package be.dpa.bootiful.activities.padp.rest;


import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;

/**
 * Activity relation service.
 *
 * @author denis
 */
@Slf4j
@Service
class RelationService {

    /**
     * Converts the passed link to a URI.
     *
     * @param link the link to convert to a URI
     * @return the URI for the link or an empty optional
     */
    public Optional<URI> convertToUri(Link link) {
        try {
            URI uri = new URI(link.getHref());
            return Optional.of(uri);
        } catch (URISyntaxException e) {
            log.warn(String.format("Failed to convert %s to a URI", link.getHref()), e);
            return Optional.empty();
        }
    }
}
