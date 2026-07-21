package finki.ukim.mk.dbexamapi.service.content;

import finki.ukim.mk.dbexamapi.domain.dtos.content.TaskFolderDto;
import finki.ukim.mk.dbexamapi.domain.models.content.TaskFolder;

import java.util.List;
import java.util.Optional;

public interface TaskFolderService {

    Optional<TaskFolder> findById(String id);

    TaskFolder findByIdNotNull(String id);

    List<TaskFolder> findAll();

    TaskFolder create(TaskFolderDto taskFolderDto);

    TaskFolder update(String id, TaskFolderDto taskFolderDto);

    TaskFolder deleteById(String id);
}
