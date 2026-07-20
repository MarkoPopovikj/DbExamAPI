package finki.ukim.mk.dbexamapi.domain.enums;

/**
 * CREATING -> AVAILABLE -> ASSIGNED -> DISCARDED -> DROPPED, or FAILED.
 * Rows are never reused or deleted — the discard trail is history.
 */
public enum ManagedDatabaseStatus {
    CREATING,
    AVAILABLE,
    ASSIGNED,
    DISCARDED,
    DROPPED,
    FAILED
}
