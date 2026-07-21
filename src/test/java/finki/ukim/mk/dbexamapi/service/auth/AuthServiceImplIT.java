package finki.ukim.mk.dbexamapi.service.auth;

import finki.ukim.mk.dbexamapi.domain.dtos.auth.AuthDto;
import finki.ukim.mk.dbexamapi.domain.dtos.auth.RegisterDto;
import finki.ukim.mk.dbexamapi.domain.dtos.auth.TokenWrapperDto;
import finki.ukim.mk.dbexamapi.domain.enums.UserRole;
import finki.ukim.mk.dbexamapi.domain.exceptions.auth.InvalidCredentialsException;
import finki.ukim.mk.dbexamapi.domain.exceptions.auth.InvalidTokenException;
import finki.ukim.mk.dbexamapi.domain.exceptions.auth.PasswordsDoNotMatchException;
import finki.ukim.mk.dbexamapi.domain.exceptions.identity.UserDetailEmailAlreadyExistsException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class AuthServiceImplIT {

    @Autowired
    private AuthService authService;

    private RegisterDto registerDto(String email) {
        return new RegisterDto(email, "password-123", "password-123", "Test", "Student", null);
    }

    private String uniqueEmail() {
        return "student-" + UUID.randomUUID() + "@test.com";
    }

    @Test
    void register_createsStudentAndReturnsTokens() {
        AuthDto authDto = authService.register(registerDto(uniqueEmail()));

        assertNotNull(authDto.user().getId());
        assertEquals(UserRole.STUDENT, authDto.user().getRole());
        assertNotNull(authDto.tokens().access().token());
        assertNotNull(authDto.tokens().refresh().token());
    }

    @Test
    void register_mismatchedPasswords_throwsPasswordsDoNotMatch() {
        RegisterDto dto = new RegisterDto(uniqueEmail(), "password-123", "different", "Test", "Student", null);

        assertThrows(PasswordsDoNotMatchException.class, () -> authService.register(dto));
    }

    @Test
    void register_duplicateEmail_throwsEmailAlreadyExists() {
        String email = uniqueEmail();
        authService.register(registerDto(email));

        assertThrows(UserDetailEmailAlreadyExistsException.class,
                () -> authService.register(registerDto(email)));
    }

    @Test
    void login_correctCredentials_returnsTokensForSameUser() {
        String email = uniqueEmail();
        AuthDto registered = authService.register(registerDto(email));

        AuthDto loggedIn = authService.login(email, "password-123");

        assertEquals(registered.user().getId(), loggedIn.user().getId());
        assertNotNull(loggedIn.tokens().access().token());
    }

    @Test
    void login_wrongPassword_throwsInvalidCredentials() {
        String email = uniqueEmail();
        authService.register(registerDto(email));

        assertThrows(InvalidCredentialsException.class,
                () -> authService.login(email, "wrong-password"));
    }

    @Test
    void login_unknownEmail_throwsInvalidCredentials() {
        assertThrows(InvalidCredentialsException.class,
                () -> authService.login(uniqueEmail(), "password-123"));
    }

    @Test
    void refresh_validRefreshToken_returnsNewPair() {
        AuthDto registered = authService.register(registerDto(uniqueEmail()));

        TokenWrapperDto refreshed = authService.refresh(registered.tokens().refresh().token());

        assertNotNull(refreshed.access().token());
        assertNotNull(refreshed.refresh().token());
        assertNotEquals(registered.tokens().access().token(), refreshed.access().token());
    }

    @Test
    void refresh_accessTokenInsteadOfRefresh_throwsInvalidToken() {
        AuthDto registered = authService.register(registerDto(uniqueEmail()));

        assertThrows(InvalidTokenException.class,
                () -> authService.refresh(registered.tokens().access().token()));
    }
}
