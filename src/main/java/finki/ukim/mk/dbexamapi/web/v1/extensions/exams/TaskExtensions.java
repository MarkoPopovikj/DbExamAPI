package finki.ukim.mk.dbexamapi.web.v1.extensions.exams;

import finki.ukim.mk.dbexamapi.domain.dtos.exams.TaskDto;
import finki.ukim.mk.dbexamapi.domain.models.exams.Task;
import finki.ukim.mk.dbexamapi.domain.models.exams.TaskEnvironment;
import finki.ukim.mk.dbexamapi.web.v1.requests.exams.TaskRequest;
import finki.ukim.mk.dbexamapi.web.v1.responses.exams.TaskResponse;

import java.util.List;

public final class TaskExtensions {

    private TaskExtensions() {
    }

    public static TaskDto toDto(TaskRequest request, TaskEnvironment environment) {
        return new TaskDto(
                environment,
                request.position(),
                request.title(),
                request.description(),
                request.taskType(),
                request.submissionMode(),
                request.points()
        );
    }

    public static TaskResponse toResponse(Task task) {
        return new TaskResponse(
                task.getId(),
                task.getExam().getId(),
                task.getEnvironment().getId(),
                task.getPosition(),
                task.getTitle(),
                task.getDescription(),
                task.getTaskType(),
                task.getSubmissionMode(),
                task.getPoints()
        );
    }

    public static List<TaskResponse> toResponse(List<Task> tasks) {
        return tasks.stream()
                .map(TaskExtensions::toResponse)
                .toList();
    }
}
