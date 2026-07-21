package finki.ukim.mk.dbexamapi.web.v1.responses.auth;

public record TokenWrapperResponse(
        TokenResponse access,
        TokenResponse refresh
) {
}
