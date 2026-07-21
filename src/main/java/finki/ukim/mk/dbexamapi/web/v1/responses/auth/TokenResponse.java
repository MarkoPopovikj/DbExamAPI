package finki.ukim.mk.dbexamapi.web.v1.responses.auth;

import java.time.Instant;

public record TokenResponse(
        String token,
        Instant expiresAt
) {
}
