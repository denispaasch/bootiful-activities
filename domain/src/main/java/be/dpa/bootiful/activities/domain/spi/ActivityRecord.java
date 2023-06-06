package be.dpa.bootiful.activities.domain.spi;

import lombok.Data;

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

