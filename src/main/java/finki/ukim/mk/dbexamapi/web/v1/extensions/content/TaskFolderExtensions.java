package finki.ukim.mk.dbexamapi.web.v1.extensions.content;

import finki.ukim.mk.dbexamapi.domain.dtos.content.TaskFolderDto;
import finki.ukim.mk.dbexamapi.domain.models.content.Folder;
import finki.ukim.mk.dbexamapi.domain.models.content.TaskFolder;
import finki.ukim.mk.dbexamapi.domain.models.exams.Task;
import finki.ukim.mk.dbexamapi.web.v1.requests.content.TaskFolderRequest;
import finki.ukim.mk.dbexamapi.web.v1.responses.content.TaskFolderResponse;

import java.util.List;

public final class TaskFolderExtensions {

    private TaskFolderExtensions() {
    }

    public static TaskFolderDto toDto(TaskFolderRequest request, Task task, Folder folder) {
        return new TaskFolderDto(
                task,
                folder
        );
    }

    public static TaskFolderResponse toResponse(TaskFolder taskFolder) {
        return new TaskFolderResponse(
                taskFolder.getId(),
                taskFolder.getTask().getId(),
                taskFolder.getFolder().getId()
        );
    }

    public static List<TaskFolderResponse> toResponse(List<TaskFolder> taskFolders) {
        return taskFolders.stream()
                .map(TaskFolderExtensions::toResponse)
                .toList();
    }
}
