package finki.ukim.mk.dbexamapi.web.v1.requests.auth;

import jakarta.validation.constraints.NotBlank;

public record RefreshTokenRequest(
        @NotBlank String refreshToken
) {
}
