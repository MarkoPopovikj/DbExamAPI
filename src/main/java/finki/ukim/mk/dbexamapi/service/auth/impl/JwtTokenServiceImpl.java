package finki.ukim.mk.dbexamapi.service.auth.impl;

import finki.ukim.mk.dbexamapi.config.JwtProperties;
import finki.ukim.mk.dbexamapi.domain.dtos.auth.TokenDto;
import finki.ukim.mk.dbexamapi.domain.dtos.auth.TokenPayload;
import finki.ukim.mk.dbexamapi.domain.dtos.auth.TokenWrapperDto;
import finki.ukim.mk.dbexamapi.domain.enums.TokenType;
import finki.ukim.mk.dbexamapi.domain.enums.UserRole;
import finki.ukim.mk.dbexamapi.domain.exceptions.auth.InvalidTokenException;
import finki.ukim.mk.dbexamapi.domain.exceptions.auth.TokenExpiredException;
import finki.ukim.mk.dbexamapi.domain.models.identity.User;
import finki.ukim.mk.dbexamapi.service.auth.TokenService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Service
public class JwtTokenServiceImpl implements TokenService {

    private static final String CLAIM_TYPE = "type";
    private static final String CLAIM_ROLE = "role";

    private final JwtProperties jwtProperties;
    private final SecretKey accessKey;
    private final SecretKey refreshKey;

    public JwtTokenServiceImpl(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
        this.accessKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtProperties.accessSecret()));
        this.refreshKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtProperties.refreshSecret()));
    }

    @Override
    public TokenWrapperDto generateAuthTokens(User user) {
        Instant now = Instant.now();
        Instant accessExpiresAt = now.plus(jwtProperties.accessExpirationMinutes(), ChronoUnit.MINUTES);
        Instant refreshExpiresAt = now.plus(jwtProperties.refreshExpirationDays(), ChronoUnit.DAYS);

        return new TokenWrapperDto(
                new TokenDto(buildToken(user, now, accessExpiresAt, TokenType.ACCESS), accessExpiresAt),
                new TokenDto(buildToken(user, now, refreshExpiresAt, TokenType.REFRESH), refreshExpiresAt)
        );
    }

    @Override
    public TokenPayload verifyToken(String token, TokenType expectedType) {
        if (token == null || token.isBlank()) {
            throw new InvalidTokenException();
        }

        Claims claims;
        try {
            claims = Jwts.parser()
                    .verifyWith(keyFor(expectedType))
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (ExpiredJwtException exception) {
            throw new TokenExpiredException();
        } catch (JwtException | IllegalArgumentException exception) {
            throw new InvalidTokenException();
        }

        if (!expectedType.name().equals(claims.get(CLAIM_TYPE, String.class))) {
            throw new InvalidTokenException();
        }

        return new TokenPayload(
                claims.getSubject(),
                UserRole.valueOf(claims.get(CLAIM_ROLE, String.class)),
                expectedType
        );
    }

    private String buildToken(User user, Instant issuedAt, Instant expiresAt, TokenType type) {
        return Jwts.builder()
                .subject(user.getId())
                .claim(CLAIM_ROLE, user.getRole().name())
                .claim(CLAIM_TYPE, type.name())
                .issuedAt(Date.from(issuedAt))
                .expiration(Date.from(expiresAt))
                .signWith(keyFor(type))
                .compact();
    }

    private SecretKey keyFor(TokenType type) {
        return type == TokenType.ACCESS ? accessKey : refreshKey;
    }
}
