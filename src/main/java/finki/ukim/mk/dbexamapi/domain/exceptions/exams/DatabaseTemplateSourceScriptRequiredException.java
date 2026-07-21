package finki.ukim.mk.dbexamapi.domain.exceptions.exams;

import finki.ukim.mk.dbexamapi.domain.exceptions.RuleViolationException;

public class DatabaseTemplateSourceScriptRequiredException extends RuleViolationException {

    public DatabaseTemplateSourceScriptRequiredException() {
        super("Database template source script is required; the server is never the only copy.");
    }
}
