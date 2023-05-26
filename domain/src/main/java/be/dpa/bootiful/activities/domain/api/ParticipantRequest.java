package be.dpa.bootiful.activities.domain.api;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Participant request.
 *
 * @author denis
 */
@Data
public class ParticipantRequest {

    @NotNull(message = "The first name is mandatory")
    @Size(min = 1, max = 255, message = "The first name can contain from 1 up to 255 characters")
    private String firstName;

    @NotNull(message = "The last name is mandatory")
    @Size(min = 1, max = 255, message = "The last name can contain from 1 up to 255 characters")
    private String lastName;
}
