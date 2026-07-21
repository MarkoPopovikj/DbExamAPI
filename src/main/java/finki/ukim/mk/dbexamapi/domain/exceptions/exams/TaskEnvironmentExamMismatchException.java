package finki.ukim.mk.dbexamapi.domain.exceptions.exams;

import finki.ukim.mk.dbexamapi.domain.exceptions.RuleViolationException;

public class TaskEnvironmentExamMismatchException extends RuleViolationException {

    public TaskEnvironmentExamMismatchException(String taskId, String environmentId) {
        super(String.format(
                "Task with id %s cannot point to environment with id %s of a different exam; "
                        + "cross-exam movement is a copy, not a move.",
                taskId, environmentId));
    }
}
