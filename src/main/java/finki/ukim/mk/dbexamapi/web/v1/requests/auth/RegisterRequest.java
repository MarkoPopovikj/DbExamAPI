package finki.ukim.mk.dbexamapi.web.v1.requests.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
        @NotBlank @Email String email,
        @NotBlank @Size(min = 8, max = 64) String password,
        @NotBlank String confirmPassword,
        String firstName,
        String lastName,
        String index
) {
}
