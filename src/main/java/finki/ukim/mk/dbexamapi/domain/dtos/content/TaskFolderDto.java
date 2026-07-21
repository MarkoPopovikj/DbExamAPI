package finki.ukim.mk.dbexamapi.domain.dtos.content;

import finki.ukim.mk.dbexamapi.domain.models.content.Folder;
import finki.ukim.mk.dbexamapi.domain.models.exams.Task;

public record TaskFolderDto(
        Task task,
        Folder folder
) {
}
