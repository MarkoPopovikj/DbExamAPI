package finki.ukim.mk.dbexamapi.service.auth;

import finki.ukim.mk.dbexamapi.domain.dtos.auth.TokenPayload;
import finki.ukim.mk.dbexamapi.domain.dtos.auth.TokenWrapperDto;
import finki.ukim.mk.dbexamapi.domain.enums.TokenType;
import finki.ukim.mk.dbexamapi.domain.models.identity.User;

public interface TokenService {

    TokenWrapperDto generateAuthTokens(User user);

    TokenPayload verifyToken(String token, TokenType expectedType);
}
