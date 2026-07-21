package finki.ukim.mk.dbexamapi.domain.exceptions.exams;

import finki.ukim.mk.dbexamapi.domain.exceptions.RuleViolationException;

public class TaskEnvironmentExamImmutableException extends RuleViolationException {

    public TaskEnvironmentExamImmutableException(String id) {
        super(String.format("Task environment with id %s belongs to its exam forever; the exam cannot change.", id));
    }
}
