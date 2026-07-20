package finki.ukim.mk.dbexamapi.domain.exceptions.exams;

import finki.ukim.mk.dbexamapi.domain.exceptions.AlreadyExistsException;

public class CourseCodeAlreadyExistsException extends AlreadyExistsException {

    public CourseCodeAlreadyExistsException(String code) {
        super(String.format("Course with code %s already exists.", code));
    }
}
