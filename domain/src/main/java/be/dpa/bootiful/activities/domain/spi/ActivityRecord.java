package be.dpa.bootiful.activities.domain.spi;

/**
 * An activity record.
 *
 * @author denis
 */
public record ActivityRecord(String alternateKey,
                             String externalKey,
                             String action,
                             String type,
                             int noOfParticipants,
                             String details) {}

