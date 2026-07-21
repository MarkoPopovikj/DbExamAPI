package finki.ukim.mk.dbexamapi.service.auth;

import finki.ukim.mk.dbexamapi.config.JwtProperties;
import finki.ukim.mk.dbexamapi.domain.dtos.auth.TokenPayload;
import finki.ukim.mk.dbexamapi.domain.dtos.auth.TokenWrapperDto;
import finki.ukim.mk.dbexamapi.domain.enums.TokenType;
import finki.ukim.mk.dbexamapi.domain.enums.UserRole;
import finki.ukim.mk.dbexamapi.domain.exceptions.auth.InvalidTokenException;
import finki.ukim.mk.dbexamapi.domain.exceptions.auth.TokenExpiredException;
import finki.ukim.mk.dbexamapi.domain.models.identity.User;
import finki.ukim.mk.dbexamapi.service.auth.impl.JwtTokenServiceImpl;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JwtTokenServiceImplTest {

    private static final String ACCESS_SECRET = "Y64V0Ki1uY0V3ZUJhc2U2NHNlY3JldGZvcnRlc3RzMDE=";
    private static final String REFRESH_SECRET = "cmVmcmVzaHNlY3JldGZvcnRlc3RzYmFzZTY0dmFsdWUx";

    private final JwtTokenServiceImpl tokenService = new JwtTokenServiceImpl(
            new JwtProperties(ACCESS_SECRET, REFRESH_SECRET, 30, 30));

    private User user() {
        User user = new User();
        user.setId("V1StGXR8_Z5jdHi6B-myT");
        user.setRole(UserRole.STUDENT);
        return user;
    }

    @Test
    void generateAuthTokens_producesVerifiablePair() {
        TokenWrapperDto tokens = tokenService.generateAuthTokens(user());

        TokenPayload accessPayload = tokenService.verifyToken(tokens.access().token(), TokenType.ACCESS);
        assertEquals("V1StGXR8_Z5jdHi6B-myT", accessPayload.userId());
        assertEquals(UserRole.STUDENT, accessPayload.role());
        assertEquals(TokenType.ACCESS, accessPayload.type());

        TokenPayload refreshPayload = tokenService.verifyToken(tokens.refresh().token(), TokenType.REFRESH);
        assertEquals("V1StGXR8_Z5jdHi6B-myT", refreshPayload.userId());
        assertEquals(TokenType.REFRESH, refreshPayload.type());

        assertTrue(tokens.access().expiresAt().isAfter(Instant.now()));
        assertTrue(tokens.refresh().expiresAt().isAfter(tokens.access().expiresAt()));
    }

    @Test
    void verifyToken_accessTokenPresentedAsRefresh_throwsInvalidToken() {
        TokenWrapperDto tokens = tokenService.generateAuthTokens(user());

        assertThrows(InvalidTokenException.class,
                () -> tokenService.verifyToken(tokens.access().token(), TokenType.REFRESH));
    }

    @Test
    void verifyToken_refreshTokenPresentedAsAccess_throwsInvalidToken() {
        TokenWrapperDto tokens = tokenService.generateAuthTokens(user());

        assertThrows(InvalidTokenException.class,
                () -> tokenService.verifyToken(tokens.refresh().token(), TokenType.ACCESS));
    }

    @Test
    void verifyToken_tamperedToken_throwsInvalidToken() {
        TokenWrapperDto tokens = tokenService.generateAuthTokens(user());
        String token = tokens.access().token();
        char lastChar = token.charAt(token.length() - 1);
        String tampered = token.substring(0, token.length() - 1) + (lastChar == 'A' ? 'B' : 'A');

        assertThrows(InvalidTokenException.class,
                () -> tokenService.verifyToken(tampered, TokenType.ACCESS));
    }

    @Test
    void verifyToken_expiredToken_throwsTokenExpired() {
        JwtTokenServiceImpl expiredTokenService = new JwtTokenServiceImpl(
                new JwtProperties(ACCESS_SECRET, REFRESH_SECRET, -1, -1));
        TokenWrapperDto tokens = expiredTokenService.generateAuthTokens(user());

        assertThrows(TokenExpiredException.class,
                () -> expiredTokenService.verifyToken(tokens.access().token(), TokenType.ACCESS));
    }

    @Test
    void verifyToken_blankToken_throwsInvalidToken() {
        assertThrows(InvalidTokenException.class,
                () -> tokenService.verifyToken(" ", TokenType.ACCESS));
    }

    @Test
    void verifyToken_malformedToken_throwsInvalidToken() {
        assertThrows(InvalidTokenException.class,
                () -> tokenService.verifyToken("not-a-jwt", TokenType.ACCESS));
    }
}
