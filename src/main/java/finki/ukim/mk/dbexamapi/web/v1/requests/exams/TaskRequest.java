package finki.ukim.mk.dbexamapi.web.v1.requests.exams;

import finki.ukim.mk.dbexamapi.domain.enums.SubmissionMode;
import finki.ukim.mk.dbexamapi.domain.enums.TaskType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record TaskRequest(
        @NotBlank String environmentId,
        @NotNull Integer position,
        @NotBlank String title,
        @NotBlank String description,
        @NotNull TaskType taskType,
        @NotNull SubmissionMode submissionMode,
        @NotNull Integer points
) {
}
