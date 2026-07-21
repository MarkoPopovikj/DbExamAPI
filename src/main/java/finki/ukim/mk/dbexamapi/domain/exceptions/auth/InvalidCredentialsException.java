package finki.ukim.mk.dbexamapi.domain.exceptions.auth;

import finki.ukim.mk.dbexamapi.domain.exceptions.UnauthorizedException;

public class InvalidCredentialsException extends UnauthorizedException {

    public InvalidCredentialsException() {
        super("Incorrect email or password.");
    }
}
