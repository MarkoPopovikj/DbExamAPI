package finki.ukim.mk.dbexamapi.domain.exceptions.auth;

import finki.ukim.mk.dbexamapi.domain.exceptions.UnauthorizedException;

public class TokenExpiredException extends UnauthorizedException {

    public TokenExpiredException() {
        super("Token has expired.");
    }
}
