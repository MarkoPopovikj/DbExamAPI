package finki.ukim.mk.dbexamapi.web.v1.extensions.identity;

import finki.ukim.mk.dbexamapi.domain.dtos.identity.UserDto;
import finki.ukim.mk.dbexamapi.domain.models.identity.User;
import finki.ukim.mk.dbexamapi.web.v1.requests.identity.UserRequest;
import finki.ukim.mk.dbexamapi.web.v1.responses.identity.UserResponse;

import java.util.List;

public final class UserExtensions {

    private UserExtensions() {
    }

    public static UserDto toDto(UserRequest request) {
        return new UserDto(request.role());
    }

    public static UserResponse toResponse(User user) {
        return new UserResponse(user.getId(), user.getRole());
    }

    public static List<UserResponse> toResponse(List<User> users) {
        return users.stream()
                .map(UserExtensions::toResponse)
                .toList();
    }
}
