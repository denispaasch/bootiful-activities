package be.dpa.bootiful.activities.padp.rest;


import be.dpa.bootiful.activities.dm.api.Activity;
import be.dpa.bootiful.activities.dm.api.Participant;
import be.dpa.bootiful.activities.dm.api.exception.ActivityNotFoundException;
import be.dpa.bootiful.activities.dm.api.exception.ParticipantNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import static be.dpa.bootiful.activities.padp.rest.util.RelationConstants.RELATION_ACTIVITIES;
import static be.dpa.bootiful.activities.padp.rest.util.RelationConstants.RELATION_ACTIVITY;
import static be.dpa.bootiful.activities.padp.rest.util.RelationConstants.RELATION_PARTICIPANTS;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

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

    public void addActivityLinks(Activity activity) throws ActivityNotFoundException, ParticipantNotFoundException {
        Link selfLink = linkTo(methodOn(ActivityController.class)
            .getActivityBy(activity.getAlternateKey())).withSelfRel();
        Link participantsLink = linkTo(methodOn(ActivityController.class)
            .getActivityParticipantsBy(activity.getAlternateKey(), null, null)).withRel(RELATION_PARTICIPANTS).expand();
        Link activitiesLink = linkTo(methodOn(ActivityController.class)
            .getActivities(null, null, null)).withRel(RELATION_ACTIVITIES).expand();
        activity.add(selfLink, participantsLink, activitiesLink);
    }

    public void addActivityLinks(List<Activity> activities)
            throws ActivityNotFoundException, ParticipantNotFoundException {
        for (Activity activity : activities) {
            addActivityLinks(activity);
        }
    }

    public void addParticipantLinks(String activityAk, Participant participant)
        throws ActivityNotFoundException, ParticipantNotFoundException {
        Link selfLink = linkTo(methodOn(ActivityController.class)
                .getActivityParticipantBy(activityAk, participant.getAlternateKey())).withSelfRel();
        Link participantsLink = linkTo(methodOn(ActivityController.class)
                .getActivityParticipantsBy(activityAk, null, null)).withRel(RELATION_PARTICIPANTS).expand();
        Link activityLink = linkTo(methodOn(ActivityController.class)
                .getActivityBy(activityAk)).withRel(RELATION_ACTIVITY);
        Link activitiesLink = linkTo(methodOn(ActivityController.class)
                .getActivities(null, null, null)).withRel(RELATION_ACTIVITIES).expand();

        participant.add(selfLink, participantsLink, activityLink, activitiesLink);
    }

    public void addParticipantLinks(String activityAk, List<Participant> participants)
        throws ActivityNotFoundException, ParticipantNotFoundException {
        for (Participant participant : participants) {
            addParticipantLinks(activityAk, participant);
        }
    }
}
