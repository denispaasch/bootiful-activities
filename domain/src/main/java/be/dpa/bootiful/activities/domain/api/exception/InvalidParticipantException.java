package be.dpa.bootiful.activities.domain.api.exception;

/**
 * Invalid participant exception.
 *
 * @author denis
 */
public class InvalidParticipantException extends Exception {
    public InvalidParticipantException(String message) {
        super(message);
    }
}
