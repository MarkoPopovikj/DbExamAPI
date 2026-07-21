package finki.ukim.mk.dbexamapi.domain.exceptions.content;

import finki.ukim.mk.dbexamapi.domain.exceptions.AlreadyExistsException;

public class TaskFolderAlreadyExistsException extends AlreadyExistsException {

    public TaskFolderAlreadyExistsException(String taskId, String folderId) {
        super(String.format("Task folder for task with id %s and folder with id %s already exists.", taskId, folderId));
    }
}
