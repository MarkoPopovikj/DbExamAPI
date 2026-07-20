package finki.ukim.mk.dbexamapi.domain.dtos.identity;

import finki.ukim.mk.dbexamapi.domain.models.identity.User;

public record UserDetailDto(
        User user,
        String firstName,
        String lastName,
        String index,
        String email
) {
}
