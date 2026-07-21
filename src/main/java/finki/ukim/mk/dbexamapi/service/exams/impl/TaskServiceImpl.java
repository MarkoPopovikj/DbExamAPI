package finki.ukim.mk.dbexamapi.service.exams.impl;

import finki.ukim.mk.dbexamapi.domain.dtos.exams.TaskDto;
import finki.ukim.mk.dbexamapi.domain.enums.SubmissionMode;
import finki.ukim.mk.dbexamapi.domain.enums.TaskType;
import finki.ukim.mk.dbexamapi.domain.exceptions.exams.TaskDoesNotExistException;
import finki.ukim.mk.dbexamapi.domain.exceptions.exams.TaskEnvironmentExamMismatchException;
import finki.ukim.mk.dbexamapi.domain.exceptions.exams.TaskEnvironmentRetiredException;
import finki.ukim.mk.dbexamapi.domain.exceptions.exams.TaskPositionAlreadyExistsException;
import finki.ukim.mk.dbexamapi.domain.exceptions.exams.TaskSubmissionModeInvalidException;
import finki.ukim.mk.dbexamapi.domain.models.exams.Task;
import finki.ukim.mk.dbexamapi.repository.content.TaskFolderRepository;
import finki.ukim.mk.dbexamapi.repository.exams.TaskRepository;
import finki.ukim.mk.dbexamapi.service.exams.TaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional(readOnly = true)
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final TaskFolderRepository taskFolderRepository;

    public TaskServiceImpl(TaskRepository taskRepository, TaskFolderRepository taskFolderRepository) {
        this.taskRepository = taskRepository;
        this.taskFolderRepository = taskFolderRepository;
    }

    @Override
    public Optional<Task> findById(String id) {
        return taskRepository.findById(id);
    }

    @Override
    public Task findByIdNotNull(String id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new TaskDoesNotExistException(id));
    }

    @Override
    public List<Task> findAll() {
        return taskRepository.findAll();
    }

    @Override
    @Transactional
    public Task create(TaskDto taskDto) {
        validateEnvironmentActive(taskDto);
        validateSubmissionMode(taskDto);

        String examId = taskDto.environment().getExam().getId();
        if (taskRepository.existsByExam_IdAndPosition(examId, taskDto.position())) {
            throw new TaskPositionAlreadyExistsException(examId, taskDto.position());
        }

        Task task = new Task();
        task.setExam(taskDto.environment().getExam());
        applyFields(task, taskDto);

        Task saved = taskRepository.save(task);
        log.info("Created task with id: {}", saved.getId());
        return saved;
    }

    @Override
    @Transactional
    public Task update(String id, TaskDto taskDto) {
        Task task = findByIdNotNull(id);

        if (!taskDto.environment().getExam().getId().equals(task.getExam().getId())) {
            throw new TaskEnvironmentExamMismatchException(id, taskDto.environment().getId());
        }
        validateEnvironmentActive(taskDto);
        validateSubmissionMode(taskDto);

        if (taskRepository.existsByExam_IdAndPositionAndIdNot(task.getExam().getId(), taskDto.position(), id)) {
            throw new TaskPositionAlreadyExistsException(task.getExam().getId(), taskDto.position());
        }

        applyFields(task, taskDto);

        Task saved = taskRepository.save(task);
        log.info("Updated task with id: {}", saved.getId());
        return saved;
    }

    @Override
    @Transactional
    public Task deleteById(String id) {
        Task task = findByIdNotNull(id);
        taskFolderRepository.deleteAllByTask_Id(id);
        taskRepository.delete(task);
        log.info("Deleted task with id: {} and its folder attachments", id);
        return task;
    }

    private void applyFields(Task task, TaskDto taskDto) {
        task.setEnvironment(taskDto.environment());
        task.setPosition(taskDto.position());
        task.setTitle(taskDto.title());
        task.setDescription(taskDto.description());
        task.setTaskType(taskDto.taskType());
        task.setSubmissionMode(taskDto.submissionMode());
        task.setPoints(taskDto.points());
    }

    private void validateEnvironmentActive(TaskDto taskDto) {
        if (!taskDto.environment().isActive()) {
            throw new TaskEnvironmentRetiredException(taskDto.environment().getId());
        }
    }

    private void validateSubmissionMode(TaskDto taskDto) {
        if (taskDto.taskType() == TaskType.SQL_QUERY && taskDto.submissionMode() != SubmissionMode.SQL_TEXT) {
            throw new TaskSubmissionModeInvalidException();
        }
    }
}
