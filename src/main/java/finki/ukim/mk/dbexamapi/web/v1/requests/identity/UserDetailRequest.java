package finki.ukim.mk.dbexamapi.web.v1.requests.identity;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserDetailRequest(
        @NotBlank String userId,
        String firstName,
        String lastName,
        String index,
        @Email String email
) {
}
