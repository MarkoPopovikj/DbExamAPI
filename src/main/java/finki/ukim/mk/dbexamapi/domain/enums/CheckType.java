package finki.ukim.mk.dbexamapi.domain.enums;

/**
 * Closed set — every value is a real engine capability. New usage of a value
 * is a TASK_CHECKS row; a new value here is a deliberate code change.
 */
public enum CheckType {
    RESULT_MATCH,
    SCHEMA_MATCH,
    STATE_MATCH,
    PERF_CHECK
}
