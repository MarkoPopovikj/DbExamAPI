package finki.ukim.mk.dbexamapi.domain.exceptions.exams;

import finki.ukim.mk.dbexamapi.domain.exceptions.NotFoundException;

public class TaskEnvironmentDoesNotExistException extends NotFoundException {

    public TaskEnvironmentDoesNotExistException(String id) {
        super(String.format("Task environment with id %s does not exist.", id));
    }
}
