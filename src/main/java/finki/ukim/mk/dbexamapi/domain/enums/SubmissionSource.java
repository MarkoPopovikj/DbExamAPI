package finki.ukim.mk.dbexamapi.domain.enums;

/**
 * UI — the student submitted the text through the UI.
 * PULLED — the engine extracted the answer from the student's database
 * (the DataGrip path for DB_STATE tasks).
 */
public enum SubmissionSource {
    UI,
    PULLED
}
