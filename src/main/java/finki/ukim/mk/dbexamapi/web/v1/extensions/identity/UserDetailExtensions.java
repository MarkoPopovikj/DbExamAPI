package finki.ukim.mk.dbexamapi.web.v1.extensions.identity;

import finki.ukim.mk.dbexamapi.domain.dtos.identity.UserDetailDto;
import finki.ukim.mk.dbexamapi.domain.models.User;
import finki.ukim.mk.dbexamapi.domain.models.UserDetail;
import finki.ukim.mk.dbexamapi.web.v1.requests.identity.UserDetailRequest;
import finki.ukim.mk.dbexamapi.web.v1.responses.identity.UserDetailResponse;

import java.util.List;

public final class UserDetailExtensions {

    private UserDetailExtensions() {
    }

    public static UserDetailDto toDto(UserDetailRequest request, User user) {
        return new UserDetailDto(
                user,
                request.firstName(),
                request.lastName(),
                request.index(),
                request.email()
        );
    }

    public static UserDetailResponse toResponse(UserDetail userDetail) {
        return new UserDetailResponse(
                userDetail.getId(),
                userDetail.getUser().getId(),
                userDetail.getFirstName(),
                userDetail.getLastName(),
                userDetail.getIndex(),
                userDetail.getEmail()
        );
    }

    public static List<UserDetailResponse> toResponse(List<UserDetail> userDetails) {
        return userDetails.stream()
                .map(UserDetailExtensions::toResponse)
                .toList();
    }
}
