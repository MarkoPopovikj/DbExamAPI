package finki.ukim.mk.dbexamapi.domain.exceptions.content;

import finki.ukim.mk.dbexamapi.domain.exceptions.NotFoundException;

public class ExamFolderDoesNotExistException extends NotFoundException {

    public ExamFolderDoesNotExistException(String id) {
        super(String.format("Exam folder with id %s does not exist.", id));
    }
}
