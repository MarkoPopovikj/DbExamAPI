package finki.ukim.mk.dbexamapi.web.v1.requests.exams;

import jakarta.validation.constraints.NotBlank;

import java.time.Instant;

public record ExamRequest(
        @NotBlank String courseId,
        @NotBlank String name,
        Instant startsAt,
        Instant endsAt
) {
}
