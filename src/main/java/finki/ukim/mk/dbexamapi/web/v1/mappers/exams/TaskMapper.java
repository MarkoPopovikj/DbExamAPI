package finki.ukim.mk.dbexamapi.web.v1.mappers.exams;

import finki.ukim.mk.dbexamapi.domain.models.exams.TaskEnvironment;
import finki.ukim.mk.dbexamapi.service.exams.TaskEnvironmentService;
import finki.ukim.mk.dbexamapi.service.exams.TaskService;
import finki.ukim.mk.dbexamapi.web.v1.extensions.exams.TaskExtensions;
import finki.ukim.mk.dbexamapi.web.v1.requests.exams.TaskRequest;
import finki.ukim.mk.dbexamapi.web.v1.responses.exams.TaskResponse;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TaskMapper {

    private final TaskService taskService;
    private final TaskEnvironmentService taskEnvironmentService;

    public TaskMapper(TaskService taskService, TaskEnvironmentService taskEnvironmentService) {
        this.taskService = taskService;
        this.taskEnvironmentService = taskEnvironmentService;
    }

    public List<TaskResponse> findAll() {
        return TaskExtensions.toResponse(taskService.findAll());
    }

    public TaskResponse findById(String id) {
        return TaskExtensions.toResponse(taskService.findByIdNotNull(id));
    }

    public TaskResponse create(TaskRequest request) {
        TaskEnvironment environment = taskEnvironmentService.findByIdNotNull(request.environmentId());
        return TaskExtensions.toResponse(taskService.create(TaskExtensions.toDto(request, environment)));
    }

    public TaskResponse update(String id, TaskRequest request) {
        TaskEnvironment environment = taskEnvironmentService.findByIdNotNull(request.environmentId());
        return TaskExtensions.toResponse(taskService.update(id, TaskExtensions.toDto(request, environment)));
    }

    public TaskResponse deleteById(String id) {
        return TaskExtensions.toResponse(taskService.deleteById(id));
    }
}
