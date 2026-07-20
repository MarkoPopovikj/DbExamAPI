package finki.ukim.mk.dbexamapi.web.v1.mappers.identity;

import finki.ukim.mk.dbexamapi.service.identity.UserService;
import finki.ukim.mk.dbexamapi.web.v1.extensions.identity.UserExtensions;
import finki.ukim.mk.dbexamapi.web.v1.requests.identity.UserRequest;
import finki.ukim.mk.dbexamapi.web.v1.responses.identity.UserResponse;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserMapper {

    private final UserService userService;

    public UserMapper(UserService userService) {
        this.userService = userService;
    }

    public List<UserResponse> findAll() {
        return UserExtensions.toResponse(userService.findAll());
    }

    public UserResponse findById(String id) {
        return UserExtensions.toResponse(userService.findByIdNotNull(id));
    }

    public UserResponse create(UserRequest request) {
        return UserExtensions.toResponse(userService.create(UserExtensions.toDto(request)));
    }

    public UserResponse update(String id, UserRequest request) {
        return UserExtensions.toResponse(userService.update(id, UserExtensions.toDto(request)));
    }

    public UserResponse deleteById(String id) {
        return UserExtensions.toResponse(userService.deleteById(id));
    }
}
