package be.dpa.bootiful.activities.domain.api;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.Data;

import jakarta.validation.constraints.NotNull;


/**
 * Request to create or update an activity.
 *
 * @author denis
 */
@Data
public class ActivityRequest {

    @NotNull(message = "The action is mandatory")
    @Size(min = 1, max = 255, message = "The action can contain from 1 up to 255 characters")
    private String action;

    @NotNull(message = "The type is mandatory")
    @Size(min = 1, max = 50, message = "The type can contain from 1 up to 50 characters")
    private String type;

    @NotNull(message = "The number of participants is mandatory")
    @Min(1)
    private int noOfParticipants;

    @Size(max = 255, message = "The action can contain from 1 up to 255 characters")
    private String details;
}
