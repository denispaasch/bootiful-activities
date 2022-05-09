package be.dpa.bootiful.activities.padp.rest;

import be.dpa.bootiful.activities.dm.api.ActivityModel;
import be.dpa.bootiful.activities.dm.api.ActivityRequest;
import be.dpa.bootiful.activities.dm.api.IActivityService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * Controller for the activities REST API.
 *
 * @author denis
 */
@RestController
@RequestMapping("/api/v1/activities")
@RequiredArgsConstructor
class ActivityController {

    private final IActivityService activityService;

    private final PagedResourcesAssembler<ActivityModel> activityResponsePagedResourcesAssembler;

    @GetMapping(produces = {MediaTypes.HAL_JSON_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<PagedModel<EntityModel<ActivityModel>>> getActivities(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        Page<ActivityModel> activities = activityService.getActivities(page, size);
        activities.getContent().forEach(activity -> {
            Link selfLink = linkTo(methodOn(ActivityController.class)
                    .getActivityBy(activity.getAlternateKey())).withSelfRel();
            activity.add(selfLink);
        });
        return ResponseEntity.ok(activityResponsePagedResourcesAssembler.toModel(activities));
    }

    @GetMapping(value = "/{alternateKey}", produces = {MediaTypes.HAL_JSON_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ActivityModel> getActivityBy(@PathVariable String alternateKey) {
        Optional<ActivityModel> optResponse = activityService.getActivityBy(alternateKey);
        ActivityModel activityModel = optResponse.orElse(null);
        if (activityModel == null) {
            return ResponseEntity.notFound().build();
        }
        addActivityLinks(activityModel);
        return ResponseEntity.ok(activityModel);
    }

    private void addActivityLinks(ActivityModel activityModel) {
        Link selfLink = linkTo(methodOn(ActivityController.class)
                .getActivityBy(activityModel.getAlternateKey())).withSelfRel();
        activityModel.add(selfLink);
        Link activitiesLink = linkTo(methodOn(ActivityController.class).getActivities(0, 5)).withRel("activities");
        activityModel.add(activitiesLink);
    }

    @PostMapping(produces = {MediaTypes.HAL_JSON_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> newActivity(@RequestBody ActivityRequest activityRequest) {
        ActivityModel activityModel = activityService.newActivity(activityRequest);
        addActivityLinks(activityModel);
        try {
            URI activityUri = new URI(activityModel.getRequiredLink(IanaLinkRelations.SELF).getHref());
            return ResponseEntity.created(activityUri).body(activityModel);
        } catch (URISyntaxException e) {
            return ResponseEntity.badRequest().body(
                    String.format("Failed to create URI to new activity with alternate key %s",
                            activityModel.getAlternateKey()));
        }
    }

    @PutMapping(value = "/{alternateKey}")
    public ResponseEntity<?> updateActivity(@RequestBody ActivityRequest activityRequest,
                                            @PathVariable String alternateKey) {
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
