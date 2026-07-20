package finki.ukim.mk.dbexamapi.domain.exceptions.content;

import finki.ukim.mk.dbexamapi.domain.exceptions.NotFoundException;

public class DocumentDoesNotExistException extends NotFoundException {

    public DocumentDoesNotExistException(String id) {
        super(String.format("Document with id %s does not exist.", id));
    }
}
