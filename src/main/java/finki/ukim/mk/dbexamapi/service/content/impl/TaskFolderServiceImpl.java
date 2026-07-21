package finki.ukim.mk.dbexamapi.service.content.impl;

import finki.ukim.mk.dbexamapi.domain.dtos.content.TaskFolderDto;
import finki.ukim.mk.dbexamapi.domain.exceptions.content.TaskFolderAlreadyExistsException;
import finki.ukim.mk.dbexamapi.domain.exceptions.content.TaskFolderDoesNotExistException;
import finki.ukim.mk.dbexamapi.domain.models.content.TaskFolder;
import finki.ukim.mk.dbexamapi.repository.content.TaskFolderRepository;
import finki.ukim.mk.dbexamapi.service.content.TaskFolderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional(readOnly = true)
public class TaskFolderServiceImpl implements TaskFolderService {

    private final TaskFolderRepository taskFolderRepository;

    public TaskFolderServiceImpl(TaskFolderRepository taskFolderRepository) {
        this.taskFolderRepository = taskFolderRepository;
    }

    @Override
    public Optional<TaskFolder> findById(String id) {
        return taskFolderRepository.findById(id);
    }

    @Override
    public TaskFolder findByIdNotNull(String id) {
        return taskFolderRepository.findById(id)
                .orElseThrow(() -> new TaskFolderDoesNotExistException(id));
    }

    @Override
    public List<TaskFolder> findAll() {
        return taskFolderRepository.findAll();
    }

    @Override
    @Transactional
    public TaskFolder create(TaskFolderDto taskFolderDto) {
        String taskId = taskFolderDto.task().getId();
        String folderId = taskFolderDto.folder().getId();
        if (taskFolderRepository.existsByTask_IdAndFolder_Id(taskId, folderId)) {
            throw new TaskFolderAlreadyExistsException(taskId, folderId);
        }

        TaskFolder taskFolder = new TaskFolder();
        taskFolder.setTask(taskFolderDto.task());
        taskFolder.setFolder(taskFolderDto.folder());

        TaskFolder saved = taskFolderRepository.save(taskFolder);
        log.info("Created task folder with id: {}", saved.getId());
        return saved;
    }

    @Override
    @Transactional
    public TaskFolder update(String id, TaskFolderDto taskFolderDto) {
        TaskFolder taskFolder = findByIdNotNull(id);

        String taskId = taskFolderDto.task().getId();
        String folderId = taskFolderDto.folder().getId();
        if (taskFolderRepository.existsByTask_IdAndFolder_IdAndIdNot(taskId, folderId, id)) {
            throw new TaskFolderAlreadyExistsException(taskId, folderId);
        }

        taskFolder.setTask(taskFolderDto.task());
        taskFolder.setFolder(taskFolderDto.folder());

        TaskFolder saved = taskFolderRepository.save(taskFolder);
        log.info("Updated task folder with id: {}", saved.getId());
        return saved;
    }

    @Override
    @Transactional
    public TaskFolder deleteById(String id) {
        TaskFolder taskFolder = findByIdNotNull(id);
        taskFolderRepository.delete(taskFolder);
        log.info("Deleted task folder with id: {}", id);
        return taskFolder;
    }
}
