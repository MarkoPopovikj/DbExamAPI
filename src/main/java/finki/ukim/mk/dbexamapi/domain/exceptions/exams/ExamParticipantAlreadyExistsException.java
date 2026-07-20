package finki.ukim.mk.dbexamapi.domain.exceptions.exams;

import finki.ukim.mk.dbexamapi.domain.exceptions.AlreadyExistsException;

public class ExamParticipantAlreadyExistsException extends AlreadyExistsException {

    public ExamParticipantAlreadyExistsException(String examId, String userId) {
        super(String.format("Exam participant for exam with id %s and user with id %s already exists.", examId, userId));
    }
}
