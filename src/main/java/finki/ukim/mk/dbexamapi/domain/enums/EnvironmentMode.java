package finki.ukim.mk.dbexamapi.domain.enums;

/**
 * PER_STUDENT — each participant gets their own database.
 * SHARED — one database for all students, read-only grants.
 */
public enum EnvironmentMode {
    PER_STUDENT,
    SHARED
}
