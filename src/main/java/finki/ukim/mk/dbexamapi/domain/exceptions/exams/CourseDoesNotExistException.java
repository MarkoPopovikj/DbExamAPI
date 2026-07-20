package finki.ukim.mk.dbexamapi.domain.exceptions.exams;

import finki.ukim.mk.dbexamapi.domain.exceptions.NotFoundException;

public class CourseDoesNotExistException extends NotFoundException {

    public CourseDoesNotExistException(String id) {
        super(String.format("Course with id %s does not exist.", id));
    }
}
