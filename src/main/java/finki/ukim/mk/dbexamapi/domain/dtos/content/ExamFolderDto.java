package finki.ukim.mk.dbexamapi.domain.dtos.content;

import finki.ukim.mk.dbexamapi.domain.models.Exam;
import finki.ukim.mk.dbexamapi.domain.models.Folder;

public record ExamFolderDto(
        Exam exam,
        Folder folder
) {
}
