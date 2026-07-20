package finki.ukim.mk.dbexamapi.web.v1.responses.exams;

import finki.ukim.mk.dbexamapi.domain.enums.ExamStatus;

import java.time.Instant;

public record ExamResponse(
        String id,
        String courseId,
        String name,
        Instant startsAt,
        Instant endsAt,
        ExamStatus status
) {
}
