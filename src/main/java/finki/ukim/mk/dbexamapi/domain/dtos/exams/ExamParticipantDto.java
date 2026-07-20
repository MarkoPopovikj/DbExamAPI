package finki.ukim.mk.dbexamapi.domain.dtos.exams;

import finki.ukim.mk.dbexamapi.domain.models.Exam;
import finki.ukim.mk.dbexamapi.domain.models.User;

public record ExamParticipantDto(
        Exam exam,
        User user,
        Boolean isAdmin
) {
}
