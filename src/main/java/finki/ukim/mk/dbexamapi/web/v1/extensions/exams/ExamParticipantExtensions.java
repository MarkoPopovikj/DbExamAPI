package finki.ukim.mk.dbexamapi.web.v1.extensions.exams;

import finki.ukim.mk.dbexamapi.domain.dtos.exams.ExamParticipantDto;
import finki.ukim.mk.dbexamapi.domain.models.Exam;
import finki.ukim.mk.dbexamapi.domain.models.ExamParticipant;
import finki.ukim.mk.dbexamapi.domain.models.User;
import finki.ukim.mk.dbexamapi.web.v1.requests.exams.ExamParticipantRequest;
import finki.ukim.mk.dbexamapi.web.v1.responses.exams.ExamParticipantResponse;

import java.util.List;

public final class ExamParticipantExtensions {

    private ExamParticipantExtensions() {
    }

    public static ExamParticipantDto toDto(ExamParticipantRequest request, Exam exam, User user) {
        return new ExamParticipantDto(
                exam,
                user,
                request.isAdmin()
        );
    }

    public static ExamParticipantResponse toResponse(ExamParticipant examParticipant) {
        return new ExamParticipantResponse(
                examParticipant.getId(),
                examParticipant.getExam().getId(),
                examParticipant.getUser().getId(),
                examParticipant.isAdmin(),
                examParticipant.getStartedAt(),
                examParticipant.getFinishedAt()
        );
    }

    public static List<ExamParticipantResponse> toResponse(List<ExamParticipant> examParticipants) {
        return examParticipants.stream()
                .map(ExamParticipantExtensions::toResponse)
                .toList();
    }
}
