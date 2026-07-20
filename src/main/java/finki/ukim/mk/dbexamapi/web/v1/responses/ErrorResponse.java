package finki.ukim.mk.dbexamapi.web.v1.responses;

import java.time.Instant;

public record ErrorResponse(
        int status,
        String error,
        String message,
        Instant timestamp,
        String path,
        String requestId
) {
}
