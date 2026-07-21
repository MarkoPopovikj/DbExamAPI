package finki.ukim.mk.dbexamapi.domain.exceptions.exams;

import finki.ukim.mk.dbexamapi.domain.exceptions.AlreadyExistsException;

public class DatabaseTemplateDbNameAlreadyExistsException extends AlreadyExistsException {

    public DatabaseTemplateDbNameAlreadyExistsException(String templateDbName) {
        super(String.format("Database template with template database name %s already exists.", templateDbName));
    }
}
