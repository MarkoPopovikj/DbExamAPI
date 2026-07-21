package finki.ukim.mk.dbexamapi.web.common.responses;

import java.time.Instant;

public record ApiStatusResponse(
        String status,
        String message,
        Instant timestamp,
        String database
) {
}
