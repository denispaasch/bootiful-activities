package be.dpa.bootiful.activities.dm.api.exception;

/**
 * Participant not found exception.
 *
 * @author denis
 */
public class ParticipantNotFoundException extends Exception {
    public ParticipantNotFoundException(String message) {
        super(message);
    }
}
