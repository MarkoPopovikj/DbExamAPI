package finki.ukim.mk.dbexamapi.domain.exceptions.exams;

import finki.ukim.mk.dbexamapi.domain.exceptions.NotFoundException;

public class ExamParticipantDoesNotExistException extends NotFoundException {

    public ExamParticipantDoesNotExistException(String id) {
        super(String.format("Exam participant with id %s does not exist.", id));
    }
}
