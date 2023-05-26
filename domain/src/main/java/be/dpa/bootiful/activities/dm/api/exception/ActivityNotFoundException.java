package be.dpa.bootiful.activities.dm.api.exception;

/**
 * Activity not found exception.
 *
 * @author denis
 */
public class ActivityNotFoundException extends Exception {
    public ActivityNotFoundException(String message) {
        super(message);
    }
}
