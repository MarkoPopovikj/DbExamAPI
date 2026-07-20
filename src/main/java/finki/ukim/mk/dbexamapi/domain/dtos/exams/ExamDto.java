package finki.ukim.mk.dbexamapi.domain.dtos.exams;

import finki.ukim.mk.dbexamapi.domain.models.Course;

import java.time.Instant;

public record ExamDto(
        Course course,
        String name,
        Instant startsAt,
        Instant endsAt
) {
}
