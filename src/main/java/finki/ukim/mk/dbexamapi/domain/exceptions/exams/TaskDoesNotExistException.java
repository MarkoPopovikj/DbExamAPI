package finki.ukim.mk.dbexamapi.domain.exceptions.exams;

import finki.ukim.mk.dbexamapi.domain.exceptions.NotFoundException;

public class TaskDoesNotExistException extends NotFoundException {

    public TaskDoesNotExistException(String id) {
        super(String.format("Task with id %s does not exist.", id));
    }
}
