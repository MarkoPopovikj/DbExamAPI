package finki.ukim.mk.dbexamapi.domain.exceptions;

public abstract class InUseException extends RuntimeException {

    protected InUseException(String message) {
        super(message);
    }
}
