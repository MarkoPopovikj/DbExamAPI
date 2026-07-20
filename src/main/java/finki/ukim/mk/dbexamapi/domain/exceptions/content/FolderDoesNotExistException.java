package finki.ukim.mk.dbexamapi.domain.exceptions.content;

import finki.ukim.mk.dbexamapi.domain.exceptions.NotFoundException;

public class FolderDoesNotExistException extends NotFoundException {

    public FolderDoesNotExistException(String id) {
        super(String.format("Folder with id %s does not exist.", id));
    }
}
