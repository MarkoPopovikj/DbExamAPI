package finki.ukim.mk.dbexamapi.domain.dtos.identity;

import finki.ukim.mk.dbexamapi.domain.enums.UserRole;

public record UserDto(
        UserRole role
) {
}
