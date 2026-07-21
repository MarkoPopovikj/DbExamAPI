package finki.ukim.mk.dbexamapi.domain.exceptions.exams;

import finki.ukim.mk.dbexamapi.domain.exceptions.RuleViolationException;

public class DatabaseTemplateDbNameInvalidException extends RuleViolationException {

    public DatabaseTemplateDbNameInvalidException(String templateDbName) {
        super(String.format(
                "Template database name %s is not a safe PostgreSQL identifier: "
                        + "lowercase letters, digits, and underscores only, starting with a letter or underscore, max 63 characters.",
                templateDbName));
    }
}
