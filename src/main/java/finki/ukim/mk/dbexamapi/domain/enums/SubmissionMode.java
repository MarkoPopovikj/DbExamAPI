package finki.ukim.mk.dbexamapi.domain.enums;

/**
 * SQL_TEXT — the student submits the statement through the UI.
 * DB_STATE — the engine pulls the answer from the student's database at check
 * time (schema from information_schema, procedure source from the catalog).
 * SQL_QUERY tasks must use SQL_TEXT (validated in the service layer).
 */
public enum SubmissionMode {
    SQL_TEXT,
    DB_STATE
}
