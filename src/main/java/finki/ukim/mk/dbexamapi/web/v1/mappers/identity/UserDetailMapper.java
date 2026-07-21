package finki.ukim.mk.dbexamapi.web.v1.mappers.identity;

import finki.ukim.mk.dbexamapi.domain.models.identity.User;
import finki.ukim.mk.dbexamapi.service.identity.UserDetailService;
import finki.ukim.mk.dbexamapi.service.identity.UserService;
import finki.ukim.mk.dbexamapi.web.v1.extensions.identity.UserDetailExtensions;
import finki.ukim.mk.dbexamapi.web.v1.requests.identity.UserDetailRequest;
import finki.ukim.mk.dbexamapi.web.v1.responses.identity.UserDetailResponse;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserDetailMapper {

    private final UserDetailService userDetailService;
    private final UserService userService;

    public UserDetailMapper(UserDetailService userDetailService, UserService userService) {
        this.userDetailService = userDetailService;
        this.userService = userService;
    }

    public List<UserDetailResponse> findAll() {
        return UserDetailExtensions.toResponse(userDetailService.findAll());
    }

    public UserDetailResponse findById(String id) {
        return UserDetailExtensions.toResponse(userDetailService.findByIdNotNull(id));
    }

    public UserDetailResponse create(UserDetailRequest request) {
        User user = userService.findByIdNotNull(request.userId());
        return UserDetailExtensions.toResponse(userDetailService.create(UserDetailExtensions.toDto(request, user)));
    }

    public UserDetailResponse update(String id, UserDetailRequest request) {
        User user = userService.findByIdNotNull(request.userId());
        return UserDetailExtensions.toResponse(userDetailService.update(id, UserDetailExtensions.toDto(request, user)));
    }

    public UserDetailResponse deleteById(String id) {
        return UserDetailExtensions.toResponse(userDetailService.deleteById(id));
    }
}
