package finki.ukim.mk.dbexamapi.domain.exceptions.exams;

import finki.ukim.mk.dbexamapi.domain.exceptions.NotFoundException;

public class ExamDoesNotExistException extends NotFoundException {

    public ExamDoesNotExistException(String id) {
        super(String.format("Exam with id %s does not exist.", id));
    }
}
