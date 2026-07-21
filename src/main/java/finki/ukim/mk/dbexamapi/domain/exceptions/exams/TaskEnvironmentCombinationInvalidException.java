package finki.ukim.mk.dbexamapi.domain.exceptions.exams;

import finki.ukim.mk.dbexamapi.domain.exceptions.RuleViolationException;

public class TaskEnvironmentCombinationInvalidException extends RuleViolationException {

    public TaskEnvironmentCombinationInvalidException(String reason) {
        super(String.format("Task environment combination is invalid: %s", reason));
    }
}
