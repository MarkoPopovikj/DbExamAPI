package finki.ukim.mk.dbexamapi.domain.exceptions.content;

import finki.ukim.mk.dbexamapi.domain.exceptions.NotFoundException;

public class TaskFolderDoesNotExistException extends NotFoundException {

    public TaskFolderDoesNotExistException(String id) {
        super(String.format("Task folder with id %s does not exist.", id));
    }
}
