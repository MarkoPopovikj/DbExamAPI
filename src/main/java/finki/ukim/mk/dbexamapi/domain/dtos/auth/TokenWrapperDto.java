package finki.ukim.mk.dbexamapi.domain.dtos.auth;

public record TokenWrapperDto(
        TokenDto access,
        TokenDto refresh
) {
}
