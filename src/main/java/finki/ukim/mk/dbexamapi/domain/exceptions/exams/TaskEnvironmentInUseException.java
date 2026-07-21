package finki.ukim.mk.dbexamapi.domain.exceptions.exams;

import finki.ukim.mk.dbexamapi.domain.exceptions.InUseException;

public class TaskEnvironmentInUseException extends InUseException {

    public TaskEnvironmentInUseException(String id) {
        super(String.format("Task environment with id %s cannot be retired while tasks point at it.", id));
    }
}
