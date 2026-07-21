package finki.ukim.mk.dbexamapi.domain.exceptions.exams;

import finki.ukim.mk.dbexamapi.domain.exceptions.RuleViolationException;

public class TaskEnvironmentRetiredException extends RuleViolationException {

    public TaskEnvironmentRetiredException(String id) {
        super(String.format("Task environment with id %s is retired and cannot be referenced.", id));
    }
}
