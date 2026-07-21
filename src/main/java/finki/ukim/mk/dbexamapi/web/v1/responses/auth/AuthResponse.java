package finki.ukim.mk.dbexamapi.web.v1.responses.auth;

import finki.ukim.mk.dbexamapi.web.v1.responses.identity.UserResponse;

public record AuthResponse(
        UserResponse user,
        TokenWrapperResponse tokens
) {
}
