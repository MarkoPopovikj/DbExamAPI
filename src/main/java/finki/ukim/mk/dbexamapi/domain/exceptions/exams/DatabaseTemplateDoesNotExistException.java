package finki.ukim.mk.dbexamapi.domain.exceptions.exams;

import finki.ukim.mk.dbexamapi.domain.exceptions.NotFoundException;

public class DatabaseTemplateDoesNotExistException extends NotFoundException {

    public DatabaseTemplateDoesNotExistException(String id) {
        super(String.format("Database template with id %s does not exist.", id));
    }
}
