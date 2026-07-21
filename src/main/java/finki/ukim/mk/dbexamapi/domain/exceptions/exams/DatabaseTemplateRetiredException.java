package finki.ukim.mk.dbexamapi.domain.exceptions.exams;

import finki.ukim.mk.dbexamapi.domain.exceptions.RuleViolationException;

public class DatabaseTemplateRetiredException extends RuleViolationException {

    public DatabaseTemplateRetiredException(String id) {
        super(String.format("Database template with id %s is retired and cannot be referenced.", id));
    }
}
