package finki.ukim.mk.dbexamapi.web.v1.requests.exams;

import jakarta.validation.constraints.NotBlank;

public record DatabaseTemplateRequest(
        @NotBlank String name,
        @NotBlank String templateDbName,
        @NotBlank String sourceScript
) {
}
