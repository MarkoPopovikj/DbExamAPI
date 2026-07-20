package finki.ukim.mk.dbexamapi.web.v1.responses.exams;

import java.time.Instant;

public record ExamParticipantResponse(
        String id,
        String examId,
        String userId,
        boolean isAdmin,
        Instant startedAt,
        Instant finishedAt
) {
}
