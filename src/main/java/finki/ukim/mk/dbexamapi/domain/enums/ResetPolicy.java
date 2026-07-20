package finki.ukim.mk.dbexamapi.domain.enums;

/**
 * NONE — no resets.
 * RECREATE — drop and rebuild from origin (empty or template).
 * SWAP_FROM_POOL — discard the current database, claim a pre-created one.
 * SHARED environments must not use SWAP_FROM_POOL (validated in the service layer).
 */
public enum ResetPolicy {
    NONE,
    RECREATE,
    SWAP_FROM_POOL
}
