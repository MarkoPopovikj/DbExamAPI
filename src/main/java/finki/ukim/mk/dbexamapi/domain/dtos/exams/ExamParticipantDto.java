package finki.ukim.mk.dbexamapi.domain.dtos.exams;

import finki.ukim.mk.dbexamapi.domain.models.exams.Exam;
import finki.ukim.mk.dbexamapi.domain.models.identity.User;

public record ExamParticipantDto(
        Exam exam,
        User user,
        Boolean isAdmin
) {
}
