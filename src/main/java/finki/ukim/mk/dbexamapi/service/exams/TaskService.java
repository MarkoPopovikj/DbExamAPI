package finki.ukim.mk.dbexamapi.service.exams;

import finki.ukim.mk.dbexamapi.domain.dtos.exams.TaskDto;
import finki.ukim.mk.dbexamapi.domain.models.exams.Task;

import java.util.List;
import java.util.Optional;

public interface TaskService {

    Optional<Task> findById(String id);

    Task findByIdNotNull(String id);

    List<Task> findAll();

    Task create(TaskDto taskDto);

    Task update(String id, TaskDto taskDto);

    Task deleteById(String id);
}
