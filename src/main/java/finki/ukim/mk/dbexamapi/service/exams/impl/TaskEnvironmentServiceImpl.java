package finki.ukim.mk.dbexamapi.service.exams.impl;

import finki.ukim.mk.dbexamapi.domain.dtos.exams.TaskEnvironmentDto;
import finki.ukim.mk.dbexamapi.domain.enums.EnvironmentMode;
import finki.ukim.mk.dbexamapi.domain.enums.ResetPolicy;
import finki.ukim.mk.dbexamapi.domain.exceptions.exams.DatabaseTemplateRetiredException;
import finki.ukim.mk.dbexamapi.domain.exceptions.exams.TaskEnvironmentCombinationInvalidException;
import finki.ukim.mk.dbexamapi.domain.exceptions.exams.TaskEnvironmentDoesNotExistException;
import finki.ukim.mk.dbexamapi.domain.exceptions.exams.TaskEnvironmentExamImmutableException;
import finki.ukim.mk.dbexamapi.domain.models.exams.TaskEnvironment;
import finki.ukim.mk.dbexamapi.repository.exams.TaskEnvironmentRepository;
import finki.ukim.mk.dbexamapi.service.exams.TaskEnvironmentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional(readOnly = true)
public class TaskEnvironmentServiceImpl implements TaskEnvironmentService {

    private final TaskEnvironmentRepository taskEnvironmentRepository;

    public TaskEnvironmentServiceImpl(TaskEnvironmentRepository taskEnvironmentRepository) {
        this.taskEnvironmentRepository = taskEnvironmentRepository;
    }

    @Override
    public Optional<TaskEnvironment> findById(String id) {
        return taskEnvironmentRepository.findById(id);
    }

    @Override
    public TaskEnvironment findByIdNotNull(String id) {
        return taskEnvironmentRepository.findById(id)
                .orElseThrow(() -> new TaskEnvironmentDoesNotExistException(id));
    }

    @Override
    public List<TaskEnvironment> findAll() {
        return taskEnvironmentRepository.findAllByActiveTrue();
    }

    @Override
    @Transactional
    public TaskEnvironment create(TaskEnvironmentDto taskEnvironmentDto) {
        validateRules(taskEnvironmentDto);

        TaskEnvironment taskEnvironment = new TaskEnvironment();
        taskEnvironment.setExam(taskEnvironmentDto.exam());
        applyFields(taskEnvironment, taskEnvironmentDto);
        taskEnvironment.setActive(true);

        TaskEnvironment saved = taskEnvironmentRepository.save(taskEnvironment);
        log.info("Created task environment with id: {}", saved.getId());
        return saved;
    }

    @Override
    @Transactional
    public TaskEnvironment update(String id, TaskEnvironmentDto taskEnvironmentDto) {
        TaskEnvironment taskEnvironment = findByIdNotNull(id);

        if (!taskEnvironment.getExam().getId().equals(taskEnvironmentDto.exam().getId())) {
            throw new TaskEnvironmentExamImmutableException(id);
        }
        validateRules(taskEnvironmentDto);

        applyFields(taskEnvironment, taskEnvironmentDto);

        TaskEnvironment saved = taskEnvironmentRepository.save(taskEnvironment);
        log.info("Updated task environment with id: {}", saved.getId());
        return saved;
    }

    @Override
    @Transactional
    public TaskEnvironment retireById(String id) {
        TaskEnvironment taskEnvironment = findByIdNotNull(id);
        taskEnvironment.setActive(false);

        TaskEnvironment saved = taskEnvironmentRepository.save(taskEnvironment);
        log.info("Retired task environment with id: {}", id);
        return saved;
    }

    @Override
    @Transactional
    public TaskEnvironment activateById(String id) {
        TaskEnvironment taskEnvironment = findByIdNotNull(id);
        taskEnvironment.setActive(true);

        TaskEnvironment saved = taskEnvironmentRepository.save(taskEnvironment);
        log.info("Reactivated task environment with id: {}", id);
        return saved;
    }

    private void applyFields(TaskEnvironment taskEnvironment, TaskEnvironmentDto taskEnvironmentDto) {
        taskEnvironment.setName(taskEnvironmentDto.name());
        taskEnvironment.setDescription(taskEnvironmentDto.description());
        taskEnvironment.setMode(taskEnvironmentDto.mode());
        taskEnvironment.setTemplate(taskEnvironmentDto.template());
        taskEnvironment.setResetPolicy(taskEnvironmentDto.resetPolicy());
        taskEnvironment.setPoolSize(taskEnvironmentDto.poolSize());
        taskEnvironment.setAutoPopulateScript(taskEnvironmentDto.autoPopulateScript());
    }

    private void validateRules(TaskEnvironmentDto taskEnvironmentDto) {
        boolean shared = taskEnvironmentDto.mode() == EnvironmentMode.SHARED;
        boolean swapFromPool = taskEnvironmentDto.resetPolicy() == ResetPolicy.SWAP_FROM_POOL;
        boolean hasTemplate = taskEnvironmentDto.template() != null;
        boolean hasAutoPopulateScript = taskEnvironmentDto.autoPopulateScript() != null
                && !taskEnvironmentDto.autoPopulateScript().isBlank();

        if (shared && swapFromPool) {
            throw new TaskEnvironmentCombinationInvalidException(
                    "a SHARED environment cannot use the SWAP_FROM_POOL reset policy.");
        }
        if (shared && !hasTemplate) {
            throw new TaskEnvironmentCombinationInvalidException(
                    "a SHARED environment requires a template; a shared database that starts empty is dead.");
        }
        if (swapFromPool && (taskEnvironmentDto.poolSize() == null || taskEnvironmentDto.poolSize() < 1)) {
            throw new TaskEnvironmentCombinationInvalidException(
                    "the SWAP_FROM_POOL reset policy requires a pool size of at least 1.");
        }
        if (!swapFromPool && taskEnvironmentDto.poolSize() != null) {
            throw new TaskEnvironmentCombinationInvalidException(
                    "a pool size is only allowed with the SWAP_FROM_POOL reset policy.");
        }
        if (hasAutoPopulateScript && hasTemplate) {
            throw new TaskEnvironmentCombinationInvalidException(
                    "an auto-populate script is forbidden together with a template.");
        }
        if (hasTemplate && !taskEnvironmentDto.template().isActive()) {
            throw new DatabaseTemplateRetiredException(taskEnvironmentDto.template().getId());
        }
    }
}
