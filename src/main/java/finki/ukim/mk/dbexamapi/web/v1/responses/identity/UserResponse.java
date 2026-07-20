package finki.ukim.mk.dbexamapi.web.v1.responses.identity;

import finki.ukim.mk.dbexamapi.domain.enums.UserRole;

public record UserResponse(
        String id,
        UserRole role
) {
}
