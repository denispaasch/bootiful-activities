package be.dpa.bootiful.activities.padp.rest;

import be.dpa.bootiful.activities.dm.api.ActivityRequest;
import be.dpa.bootiful.activities.dm.api.ActivityResponse;
import be.dpa.bootiful.activities.dm.api.IActivityService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.LinkRelation;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/v1/activities")
@RequiredArgsConstructor
public class ActivityController {

    private final IActivityService activityService;

    @GetMapping(produces = {MediaTypes.HAL_JSON_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<List<ActivityResponse>> getActivities() {
        List<ActivityResponse> activities = activityService.getActivities();
        activities.stream().forEach(activity -> {
            Link selfLink = linkTo(methodOn(ActivityController.class).getActivityBy(activity.getAlternateKey())).withSelfRel();
            activity.add(selfLink);
        });
        return ResponseEntity.ok(activities);
    }

    @GetMapping(value = "/{alternateKey}", produces = {MediaTypes.HAL_JSON_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ActivityResponse> getActivityBy(@PathVariable String alternateKey) {
        Optional<ActivityResponse> optResponse = activityService.getActivityBy(alternateKey);
        ActivityResponse activityResponse = optResponse.orElse(null);
        if (activityResponse == null) {
            return ResponseEntity.notFound().build();
        }
        addActivityLinks(activityResponse);
        return ResponseEntity.ok(activityResponse);
    }

    private void addActivityLinks(ActivityResponse activityResponse) {
        Link selfLink = linkTo(methodOn(ActivityController.class).getActivityBy(activityResponse.getAlternateKey())).withSelfRel();
        activityResponse.add(selfLink);
        Link activitiesLink = linkTo(methodOn(ActivityController.class).getActivities()).withRel("activities");
        activityResponse.add(activitiesLink);
    }

    @PostMapping(produces = {MediaTypes.HAL_JSON_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> newActivity(@RequestBody ActivityRequest activityRequest) {
        ActivityResponse activityResponse = activityService.newActivity(activityRequest);
        addActivityLinks(activityResponse);
        try {
            return ResponseEntity.created(new URI(
                    activityResponse.getRequiredLink(IanaLinkRelations.SELF).getHref())).body(activityResponse);
        } catch (URISyntaxException e) {
            return ResponseEntity.badRequest().body(
                    String.format("Failed to create URI to new activity with alternate key %s", activityResponse.getAlternateKey()));
        }
    }

    @PutMapping(value = "/{alternateKey}")
    public ResponseEntity<?> updateActivity(@RequestBody ActivityRequest activityRequest, @PathVariable String alternateKey) {
        activityService.updateActivity(alternateKey, activityRequest);
        Link activityLink = linkTo(methodOn(ActivityController.class).getActivityBy(alternateKey)).withSelfRel();
        try {
            return ResponseEntity.noContent().location(new URI(activityLink.getHref())).build();
        } catch (URISyntaxException e) {
            return ResponseEntity.badRequest().body(
                    String.format("Failed to create URI to updated activity with alternate key %s", alternateKey));
        }
    }

    @DeleteMapping(value = "/{alternateKey}")
    public ResponseEntity<?> deleteActivity(@PathVariable String alternateKey) {
        activityService.deleteActivity(alternateKey);
        return ResponseEntity.noContent().build();
    }

}
