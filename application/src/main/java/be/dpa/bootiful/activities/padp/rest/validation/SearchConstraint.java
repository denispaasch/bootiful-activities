package be.dpa.bootiful.activities.padp.rest.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Search filter constraint.
 *
 * @author denis
 */
@Documented
@Constraint(validatedBy = SearchValidator.class)
@Target(value = ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface SearchConstraint {

    /**
     * Message to show when the validation fails.
     *
     * @return the message
     */
    String message() default "Invalid search filter";

    /**
     * Groups of the message to show when the validation fails.
     *
     * @return the groups
     */
    Class<?>[] groups() default {};

    /**
     * Gets the payload.
     *
     * @return the payload
     */
    Class<? extends Payload>[] payload() default {};
}
