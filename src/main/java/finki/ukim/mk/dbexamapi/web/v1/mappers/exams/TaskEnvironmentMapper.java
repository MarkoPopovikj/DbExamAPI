package finki.ukim.mk.dbexamapi.web.v1.mappers.exams;

import finki.ukim.mk.dbexamapi.domain.models.exams.DatabaseTemplate;
import finki.ukim.mk.dbexamapi.domain.models.exams.Exam;
import finki.ukim.mk.dbexamapi.service.exams.DatabaseTemplateService;
import finki.ukim.mk.dbexamapi.service.exams.ExamService;
import finki.ukim.mk.dbexamapi.service.exams.TaskEnvironmentService;
import finki.ukim.mk.dbexamapi.web.v1.extensions.exams.TaskEnvironmentExtensions;
import finki.ukim.mk.dbexamapi.web.v1.requests.exams.TaskEnvironmentRequest;
import finki.ukim.mk.dbexamapi.web.v1.responses.exams.TaskEnvironmentResponse;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TaskEnvironmentMapper {

    private final TaskEnvironmentService taskEnvironmentService;
    private final ExamService examService;
    private final DatabaseTemplateService databaseTemplateService;

    public TaskEnvironmentMapper(TaskEnvironmentService taskEnvironmentService,
                                 ExamService examService,
                                 DatabaseTemplateService databaseTemplateService) {
        this.taskEnvironmentService = taskEnvironmentService;
        this.examService = examService;
        this.databaseTemplateService = databaseTemplateService;
    }

    public List<TaskEnvironmentResponse> findAll() {
        return TaskEnvironmentExtensions.toResponse(taskEnvironmentService.findAll());
    }

    public TaskEnvironmentResponse findById(String id) {
        return TaskEnvironmentExtensions.toResponse(taskEnvironmentService.findByIdNotNull(id));
    }

    public TaskEnvironmentResponse create(TaskEnvironmentRequest request) {
        Exam exam = examService.findByIdNotNull(request.examId());
        DatabaseTemplate template = resolveTemplate(request);
        return TaskEnvironmentExtensions.toResponse(
                taskEnvironmentService.create(TaskEnvironmentExtensions.toDto(request, exam, template)));
    }

    public TaskEnvironmentResponse update(String id, TaskEnvironmentRequest request) {
        Exam exam = examService.findByIdNotNull(request.examId());
        DatabaseTemplate template = resolveTemplate(request);
        return TaskEnvironmentExtensions.toResponse(
                taskEnvironmentService.update(id, TaskEnvironmentExtensions.toDto(request, exam, template)));
    }

    public TaskEnvironmentResponse retireById(String id) {
        return TaskEnvironmentExtensions.toResponse(taskEnvironmentService.retireById(id));
    }

    public TaskEnvironmentResponse activateById(String id) {
        return TaskEnvironmentExtensions.toResponse(taskEnvironmentService.activateById(id));
    }

    private DatabaseTemplate resolveTemplate(TaskEnvironmentRequest request) {
        return request.templateId() == null
                ? null
                : databaseTemplateService.findByIdNotNull(request.templateId());
    }
}
