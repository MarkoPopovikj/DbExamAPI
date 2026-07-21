package finki.ukim.mk.dbexamapi.web.v1.extensions.auth;

import finki.ukim.mk.dbexamapi.domain.dtos.auth.AuthDto;
import finki.ukim.mk.dbexamapi.domain.dtos.auth.RegisterDto;
import finki.ukim.mk.dbexamapi.domain.dtos.auth.TokenDto;
import finki.ukim.mk.dbexamapi.domain.dtos.auth.TokenWrapperDto;
import finki.ukim.mk.dbexamapi.web.v1.extensions.identity.UserExtensions;
import finki.ukim.mk.dbexamapi.web.v1.requests.auth.RegisterRequest;
import finki.ukim.mk.dbexamapi.web.v1.responses.auth.AuthResponse;
import finki.ukim.mk.dbexamapi.web.v1.responses.auth.TokenResponse;
import finki.ukim.mk.dbexamapi.web.v1.responses.auth.TokenWrapperResponse;

public final class AuthExtensions {

    private AuthExtensions() {
    }

    public static RegisterDto toDto(RegisterRequest request) {
        return new RegisterDto(
                request.email(),
                request.password(),
                request.confirmPassword(),
                request.firstName(),
                request.lastName(),
                request.index()
        );
    }

    public static AuthResponse toResponse(AuthDto authDto) {
        return new AuthResponse(
                UserExtensions.toResponse(authDto.user()),
                toResponse(authDto.tokens())
        );
    }

    public static TokenWrapperResponse toResponse(TokenWrapperDto tokens) {
        return new TokenWrapperResponse(
                toResponse(tokens.access()),
                toResponse(tokens.refresh())
        );
    }

    private static TokenResponse toResponse(TokenDto token) {
        return new TokenResponse(token.token(), token.expiresAt());
    }
}
