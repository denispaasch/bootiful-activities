package be.dpa.bootiful.activities.padp.rest;

import be.dpa.bootiful.activities.dm.api.ApiEntrypoint;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static be.dpa.bootiful.activities.padp.rest.util.RelationConstants.RELATION_ACTIVITIES;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * API entry point controller to access the bootiful activities.
 *
 * @author denis
 */
@RestController
@RequestMapping("/api/v1")
class ApiEntrypointController {

    /**
     * Gets the API entry point to access the bootiful activities.
     *
     * @return the root response
     */
    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<ApiEntrypoint> getRoot() {
        ApiEntrypoint apiEntryPoint = new ApiEntrypoint();
        Link activitiesLink = linkTo(methodOn(ActivityController.class).getActivities(null, null, null))
                .withRel(RELATION_ACTIVITIES).expand();
        apiEntryPoint.add(activitiesLink);
        return ResponseEntity.ok(apiEntryPoint);
    }
}
