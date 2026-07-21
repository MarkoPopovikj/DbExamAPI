package finki.ukim.mk.dbexamapi.domain.exceptions.auth;

import finki.ukim.mk.dbexamapi.domain.exceptions.UnauthorizedException;

public class InvalidTokenException extends UnauthorizedException {

    public InvalidTokenException() {
        super("Token is not valid.");
    }
}
