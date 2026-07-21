package finki.ukim.mk.dbexamapi.web.v1.controllers.auth;

import finki.ukim.mk.dbexamapi.web.v1.mappers.auth.AuthMapper;
import finki.ukim.mk.dbexamapi.web.v1.requests.auth.LoginRequest;
import finki.ukim.mk.dbexamapi.web.v1.requests.auth.RefreshTokenRequest;
import finki.ukim.mk.dbexamapi.web.v1.requests.auth.RegisterRequest;
import finki.ukim.mk.dbexamapi.web.v1.responses.auth.AuthResponse;
import finki.ukim.mk.dbexamapi.web.v1.responses.auth.TokenWrapperResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/auth")
public class AuthController {

    private final AuthMapper authMapper;

    public AuthController(AuthMapper authMapper) {
        this.authMapper = authMapper;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authMapper.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authMapper.login(request));
    }

    @PostMapping("/refresh-tokens")
    public ResponseEntity<TokenWrapperResponse> refreshTokens(@Valid @RequestBody RefreshTokenRequest request) {
        return ResponseEntity.ok(authMapper.refresh(request));
    }
}
