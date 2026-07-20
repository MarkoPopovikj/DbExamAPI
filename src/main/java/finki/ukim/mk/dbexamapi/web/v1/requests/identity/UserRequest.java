package finki.ukim.mk.dbexamapi.web.v1.requests.identity;

import finki.ukim.mk.dbexamapi.domain.enums.UserRole;
import jakarta.validation.constraints.NotNull;

public record UserRequest(
        @NotNull UserRole role
) {
}
