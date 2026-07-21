package finki.ukim.mk.dbexamapi.domain.exceptions.exams;

import finki.ukim.mk.dbexamapi.domain.exceptions.InUseException;

public class DatabaseTemplateInUseException extends InUseException {

    public DatabaseTemplateInUseException(String id) {
        super(String.format(
                "Database template with id %s cannot be retired while an active task environment references it.", id));
    }
}
