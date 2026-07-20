package finki.ukim.mk.dbexamapi.web.v1.requests.content;

import jakarta.validation.constraints.NotBlank;

public record DocumentRequest(
        @NotBlank String folderId,
        String name,
        String documentMd
) {
}
