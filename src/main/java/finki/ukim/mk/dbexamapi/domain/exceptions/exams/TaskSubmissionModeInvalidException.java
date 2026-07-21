package finki.ukim.mk.dbexamapi.domain.exceptions.exams;

import finki.ukim.mk.dbexamapi.domain.exceptions.RuleViolationException;

public class TaskSubmissionModeInvalidException extends RuleViolationException {

    public TaskSubmissionModeInvalidException() {
        super("SQL_QUERY tasks must use the SQL_TEXT submission mode; "
                + "a query answer cannot be pulled out of database state.");
    }
}
