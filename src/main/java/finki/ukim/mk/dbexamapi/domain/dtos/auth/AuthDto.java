package finki.ukim.mk.dbexamapi.domain.dtos.auth;

import finki.ukim.mk.dbexamapi.domain.models.identity.User;

public record AuthDto(
        User user,
        TokenWrapperDto tokens
) {
}
