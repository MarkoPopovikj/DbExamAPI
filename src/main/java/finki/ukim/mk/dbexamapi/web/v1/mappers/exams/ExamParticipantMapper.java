package finki.ukim.mk.dbexamapi.web.v1.mappers.exams;

import finki.ukim.mk.dbexamapi.domain.models.exams.Exam;
import finki.ukim.mk.dbexamapi.domain.models.identity.User;
import finki.ukim.mk.dbexamapi.service.exams.ExamParticipantService;
import finki.ukim.mk.dbexamapi.service.exams.ExamService;
import finki.ukim.mk.dbexamapi.service.identity.UserService;
import finki.ukim.mk.dbexamapi.web.v1.extensions.exams.ExamParticipantExtensions;
import finki.ukim.mk.dbexamapi.web.v1.requests.exams.ExamParticipantRequest;
import finki.ukim.mk.dbexamapi.web.v1.responses.exams.ExamParticipantResponse;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ExamParticipantMapper {

    private final ExamParticipantService examParticipantService;
    private final ExamService examService;
    private final UserService userService;

    public ExamParticipantMapper(ExamParticipantService examParticipantService,
                                 ExamService examService,
                                 UserService userService) {
        this.examParticipantService = examParticipantService;
        this.examService = examService;
        this.userService = userService;
    }

    public List<ExamParticipantResponse> findAll() {
        return ExamParticipantExtensions.toResponse(examParticipantService.findAll());
    }

    public ExamParticipantResponse findById(String id) {
        return ExamParticipantExtensions.toResponse(examParticipantService.findByIdNotNull(id));
    }

    public ExamParticipantResponse create(ExamParticipantRequest request) {
        Exam exam = examService.findByIdNotNull(request.examId());
        User user = userService.findByIdNotNull(request.userId());
        return ExamParticipantExtensions.toResponse(
                examParticipantService.create(ExamParticipantExtensions.toDto(request, exam, user)));
    }

    public ExamParticipantResponse update(String id, ExamParticipantRequest request) {
        Exam exam = examService.findByIdNotNull(request.examId());
        User user = userService.findByIdNotNull(request.userId());
        return ExamParticipantExtensions.toResponse(
                examParticipantService.update(id, ExamParticipantExtensions.toDto(request, exam, user)));
    }

    public ExamParticipantResponse deleteById(String id) {
        return ExamParticipantExtensions.toResponse(examParticipantService.deleteById(id));
    }
}
