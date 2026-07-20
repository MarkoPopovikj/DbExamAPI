package finki.ukim.mk.dbexamapi.domain.exceptions;

public abstract class NotFoundException extends RuntimeException {

    protected NotFoundException(String message) {
        super(message);
    }
}
