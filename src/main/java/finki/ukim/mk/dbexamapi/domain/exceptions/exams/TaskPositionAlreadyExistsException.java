package finki.ukim.mk.dbexamapi.domain.exceptions.exams;

import finki.ukim.mk.dbexamapi.domain.exceptions.AlreadyExistsException;

public class TaskPositionAlreadyExistsException extends AlreadyExistsException {

    public TaskPositionAlreadyExistsException(String examId, int position) {
        super(String.format("Task at position %d in exam with id %s already exists.", position, examId));
    }
}
