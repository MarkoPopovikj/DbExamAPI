package finki.ukim.mk.dbexamapi.web.v1.mappers.content;

import finki.ukim.mk.dbexamapi.domain.models.content.Folder;
import finki.ukim.mk.dbexamapi.domain.models.exams.Task;
import finki.ukim.mk.dbexamapi.service.content.FolderService;
import finki.ukim.mk.dbexamapi.service.content.TaskFolderService;
import finki.ukim.mk.dbexamapi.service.exams.TaskService;
import finki.ukim.mk.dbexamapi.web.v1.extensions.content.TaskFolderExtensions;
import finki.ukim.mk.dbexamapi.web.v1.requests.content.TaskFolderRequest;
import finki.ukim.mk.dbexamapi.web.v1.responses.content.TaskFolderResponse;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TaskFolderMapper {

    private final TaskFolderService taskFolderService;
    private final TaskService taskService;
    private final FolderService folderService;

    public TaskFolderMapper(TaskFolderService taskFolderService,
                            TaskService taskService,
                            FolderService folderService) {
        this.taskFolderService = taskFolderService;
        this.taskService = taskService;
        this.folderService = folderService;
    }

    public List<TaskFolderResponse> findAll() {
        return TaskFolderExtensions.toResponse(taskFolderService.findAll());
    }

    public TaskFolderResponse findById(String id) {
        return TaskFolderExtensions.toResponse(taskFolderService.findByIdNotNull(id));
    }

    public TaskFolderResponse create(TaskFolderRequest request) {
        Task task = taskService.findByIdNotNull(request.taskId());
        Folder folder = folderService.findByIdNotNull(request.folderId());
        return TaskFolderExtensions.toResponse(
                taskFolderService.create(TaskFolderExtensions.toDto(request, task, folder)));
    }

    public TaskFolderResponse update(String id, TaskFolderRequest request) {
        Task task = taskService.findByIdNotNull(request.taskId());
        Folder folder = folderService.findByIdNotNull(request.folderId());
        return TaskFolderExtensions.toResponse(
                taskFolderService.update(id, TaskFolderExtensions.toDto(request, task, folder)));
    }

    public TaskFolderResponse deleteById(String id) {
        return TaskFolderExtensions.toResponse(taskFolderService.deleteById(id));
    }
}
