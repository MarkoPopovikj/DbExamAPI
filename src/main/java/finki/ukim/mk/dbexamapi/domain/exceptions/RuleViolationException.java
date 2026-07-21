package finki.ukim.mk.dbexamapi.domain.exceptions;

public abstract class RuleViolationException extends RuntimeException {

    protected RuleViolationException(String message) {
        super(message);
    }
}
