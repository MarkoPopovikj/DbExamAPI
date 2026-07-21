package finki.ukim.mk.dbexamapi.web.v1.mappers.auth;

import finki.ukim.mk.dbexamapi.service.auth.AuthService;
import finki.ukim.mk.dbexamapi.web.v1.extensions.auth.AuthExtensions;
import finki.ukim.mk.dbexamapi.web.v1.requests.auth.LoginRequest;
import finki.ukim.mk.dbexamapi.web.v1.requests.auth.RefreshTokenRequest;
import finki.ukim.mk.dbexamapi.web.v1.requests.auth.RegisterRequest;
import finki.ukim.mk.dbexamapi.web.v1.responses.auth.AuthResponse;
import finki.ukim.mk.dbexamapi.web.v1.responses.auth.TokenWrapperResponse;
import org.springframework.stereotype.Component;

@Component
public class AuthMapper {

    private final AuthService authService;

    public AuthMapper(AuthService authService) {
        this.authService = authService;
    }

    public AuthResponse register(RegisterRequest request) {
        return AuthExtensions.toResponse(authService.register(AuthExtensions.toDto(request)));
    }

    public AuthResponse login(LoginRequest request) {
        return AuthExtensions.toResponse(authService.login(request.email(), request.password()));
    }

    public TokenWrapperResponse refresh(RefreshTokenRequest request) {
        return AuthExtensions.toResponse(authService.refresh(request.refreshToken()));
    }
}
