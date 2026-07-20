package finki.ukim.mk.dbexamapi.web.v1.responses.identity;

public record UserDetailResponse(
        String id,
        String userId,
        String firstName,
        String lastName,
        String index,
        String email
) {
}
