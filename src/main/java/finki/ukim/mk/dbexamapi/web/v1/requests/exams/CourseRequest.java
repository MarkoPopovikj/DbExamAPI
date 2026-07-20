package finki.ukim.mk.dbexamapi.web.v1.requests.exams;

import jakarta.validation.constraints.NotBlank;

public record CourseRequest(
        @NotBlank String code,
        @NotBlank String name
) {
}
