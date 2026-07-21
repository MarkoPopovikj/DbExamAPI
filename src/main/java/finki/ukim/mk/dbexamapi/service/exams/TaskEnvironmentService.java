package finki.ukim.mk.dbexamapi.service.exams;

import finki.ukim.mk.dbexamapi.domain.dtos.exams.TaskEnvironmentDto;
import finki.ukim.mk.dbexamapi.domain.models.exams.TaskEnvironment;

import java.util.List;
import java.util.Optional;

public interface TaskEnvironmentService {

    Optional<TaskEnvironment> findById(String id);

    TaskEnvironment findByIdNotNull(String id);

    List<TaskEnvironment> findAll();

    TaskEnvironment create(TaskEnvironmentDto taskEnvironmentDto);

    TaskEnvironment update(String id, TaskEnvironmentDto taskEnvironmentDto);

    TaskEnvironment retireById(String id);

    TaskEnvironment activateById(String id);
}
