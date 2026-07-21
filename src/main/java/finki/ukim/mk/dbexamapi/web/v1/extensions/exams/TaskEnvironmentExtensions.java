package finki.ukim.mk.dbexamapi.web.v1.extensions.exams;

import finki.ukim.mk.dbexamapi.domain.dtos.exams.TaskEnvironmentDto;
import finki.ukim.mk.dbexamapi.domain.models.exams.DatabaseTemplate;
import finki.ukim.mk.dbexamapi.domain.models.exams.Exam;
import finki.ukim.mk.dbexamapi.domain.models.exams.TaskEnvironment;
import finki.ukim.mk.dbexamapi.web.v1.requests.exams.TaskEnvironmentRequest;
import finki.ukim.mk.dbexamapi.web.v1.responses.exams.TaskEnvironmentResponse;

import java.util.List;

public final class TaskEnvironmentExtensions {

    private TaskEnvironmentExtensions() {
    }

    public static TaskEnvironmentDto toDto(TaskEnvironmentRequest request, Exam exam, DatabaseTemplate template) {
        return new TaskEnvironmentDto(
                exam,
                request.name(),
                request.description(),
                request.mode(),
                template,
                request.resetPolicy(),
                request.poolSize(),
                request.autoPopulateScript()
        );
    }

    public static TaskEnvironmentResponse toResponse(TaskEnvironment taskEnvironment) {
        return new TaskEnvironmentResponse(
                taskEnvironment.getId(),
                taskEnvironment.getExam().getId(),
                taskEnvironment.getName(),
                taskEnvironment.getDescription(),
                taskEnvironment.getMode(),
                taskEnvironment.getTemplate() == null ? null : taskEnvironment.getTemplate().getId(),
                taskEnvironment.getResetPolicy(),
                taskEnvironment.getPoolSize(),
                taskEnvironment.getAutoPopulateScript(),
                taskEnvironment.isActive()
        );
    }

    public static List<TaskEnvironmentResponse> toResponse(List<TaskEnvironment> taskEnvironments) {
        return taskEnvironments.stream()
                .map(TaskEnvironmentExtensions::toResponse)
                .toList();
    }
}
