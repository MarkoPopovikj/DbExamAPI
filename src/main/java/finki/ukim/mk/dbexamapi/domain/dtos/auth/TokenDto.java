package finki.ukim.mk.dbexamapi.domain.dtos.auth;

import java.time.Instant;

public record TokenDto(
        String token,
        Instant expiresAt
) {
}
