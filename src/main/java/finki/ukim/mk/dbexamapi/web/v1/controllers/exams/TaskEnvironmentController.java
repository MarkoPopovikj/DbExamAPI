package finki.ukim.mk.dbexamapi.web.v1.controllers.exams;

import finki.ukim.mk.dbexamapi.web.v1.mappers.exams.TaskEnvironmentMapper;
import finki.ukim.mk.dbexamapi.web.v1.requests.exams.TaskEnvironmentRequest;
import finki.ukim.mk.dbexamapi.web.v1.responses.exams.TaskEnvironmentResponse;
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
@RequestMapping("/v1/task-environments")
public class TaskEnvironmentController {

    private final TaskEnvironmentMapper taskEnvironmentMapper;

    public TaskEnvironmentController(TaskEnvironmentMapper taskEnvironmentMapper) {
        this.taskEnvironmentMapper = taskEnvironmentMapper;
    }

    @GetMapping
    public ResponseEntity<List<TaskEnvironmentResponse>> findAll() {
        return ResponseEntity.ok(taskEnvironmentMapper.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskEnvironmentResponse> findById(@PathVariable String id) {
        return ResponseEntity.ok(taskEnvironmentMapper.findById(id));
    }

    @PostMapping
    public ResponseEntity<TaskEnvironmentResponse> create(@Valid @RequestBody TaskEnvironmentRequest request) {
        return ResponseEntity.ok(taskEnvironmentMapper.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskEnvironmentResponse> update(@PathVariable String id,
                                                          @Valid @RequestBody TaskEnvironmentRequest request) {
        return ResponseEntity.ok(taskEnvironmentMapper.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<TaskEnvironmentResponse> retireById(@PathVariable String id) {
        return ResponseEntity.ok(taskEnvironmentMapper.retireById(id));
    }

    @PostMapping("/{id}/activate")
    public ResponseEntity<TaskEnvironmentResponse> activateById(@PathVariable String id) {
        return ResponseEntity.ok(taskEnvironmentMapper.activateById(id));
    }
}
