package finki.ukim.mk.dbexamapi.domain.exceptions.content;

import finki.ukim.mk.dbexamapi.domain.exceptions.AlreadyExistsException;

public class ExamFolderAlreadyExistsException extends AlreadyExistsException {

    public ExamFolderAlreadyExistsException(String examId, String folderId) {
        super(String.format("Exam folder for exam with id %s and folder with id %s already exists.", examId, folderId));
    }
}
