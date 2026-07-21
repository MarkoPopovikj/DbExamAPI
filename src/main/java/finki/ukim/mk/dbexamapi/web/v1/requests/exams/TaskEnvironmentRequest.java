package finki.ukim.mk.dbexamapi.web.v1.requests.exams;

import finki.ukim.mk.dbexamapi.domain.enums.EnvironmentMode;
import finki.ukim.mk.dbexamapi.domain.enums.ResetPolicy;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record TaskEnvironmentRequest(
        @NotBlank String examId,
        @NotBlank String name,
        String description,
        @NotNull EnvironmentMode mode,
        String templateId,
        @NotNull ResetPolicy resetPolicy,
        Integer poolSize,
        String autoPopulateScript
) {
}
