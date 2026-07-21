package finki.ukim.mk.dbexamapi.domain.dtos.auth;

public record RegisterDto(
        String email,
        String password,
        String confirmPassword,
        String firstName,
        String lastName,
        String index
) {
}
