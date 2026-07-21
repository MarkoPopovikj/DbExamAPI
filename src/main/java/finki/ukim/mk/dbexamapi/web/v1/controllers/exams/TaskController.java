package finki.ukim.mk.dbexamapi.web.v1.controllers.exams;

import finki.ukim.mk.dbexamapi.web.v1.mappers.exams.TaskMapper;
import finki.ukim.mk.dbexamapi.web.v1.requests.exams.TaskRequest;
import finki.ukim.mk.dbexamapi.web.v1.responses.exams.TaskResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1/tasks")
public class TaskController {

    private final TaskMapper taskMapper;

    public TaskController(TaskMapper taskMapper) {
        this.taskMapper = taskMapper;
    }

    @GetMapping
    public ResponseEntity<List<TaskResponse>> findAll() {
        return ResponseEntity.ok(taskMapper.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskResponse> findById(@PathVariable String id) {
        return ResponseEntity.ok(taskMapper.findById(id));
    }

    @PostMapping
    public ResponseEntity<TaskResponse> create(@Valid @RequestBody TaskRequest request) {
        return ResponseEntity.ok(taskMapper.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskResponse> update(@PathVariable String id,
                                               @Valid @RequestBody TaskRequest request) {
        return ResponseEntity.ok(taskMapper.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<TaskResponse> deleteById(@PathVariable String id) {
        return ResponseEntity.ok(taskMapper.deleteById(id));
    }
}
