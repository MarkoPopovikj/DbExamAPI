package finki.ukim.mk.dbexamapi.domain.exceptions;

public abstract class UnauthorizedException extends RuntimeException {

    protected UnauthorizedException(String message) {
        super(message);
    }
}
