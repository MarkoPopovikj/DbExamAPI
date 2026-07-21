package finki.ukim.mk.dbexamapi.domain.exceptions.auth;

import finki.ukim.mk.dbexamapi.domain.exceptions.RuleViolationException;

public class PasswordsDoNotMatchException extends RuleViolationException {

    public PasswordsDoNotMatchException() {
        super("Passwords do not match.");
    }
}
