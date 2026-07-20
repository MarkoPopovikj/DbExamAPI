package finki.ukim.mk.dbexamapi.domain.exceptions;

public abstract class AlreadyExistsException extends RuntimeException {

    protected AlreadyExistsException(String message) {
        super(message);
    }
}
