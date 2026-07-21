package finki.ukim.mk.dbexamapi.domain.dtos.auth;

import finki.ukim.mk.dbexamapi.domain.enums.TokenType;
import finki.ukim.mk.dbexamapi.domain.enums.UserRole;

public record TokenPayload(
        String userId,
        UserRole role,
        TokenType type
) {
}
