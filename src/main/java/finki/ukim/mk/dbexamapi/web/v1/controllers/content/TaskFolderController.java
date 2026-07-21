package finki.ukim.mk.dbexamapi.web.v1.controllers.content;

import finki.ukim.mk.dbexamapi.web.v1.mappers.content.TaskFolderMapper;
import finki.ukim.mk.dbexamapi.web.v1.requests.content.TaskFolderRequest;
import finki.ukim.mk.dbexamapi.web.v1.responses.content.TaskFolderResponse;
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
@RequestMapping("/v1/task-folders")
public class TaskFolderController {

    private final TaskFolderMapper taskFolderMapper;

    public TaskFolderController(TaskFolderMapper taskFolderMapper) {
        this.taskFolderMapper = taskFolderMapper;
    }

    @GetMapping
    public ResponseEntity<List<TaskFolderResponse>> findAll() {
        return ResponseEntity.ok(taskFolderMapper.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskFolderResponse> findById(@PathVariable String id) {
        return ResponseEntity.ok(taskFolderMapper.findById(id));
    }

    @PostMapping
    public ResponseEntity<TaskFolderResponse> create(@Valid @RequestBody TaskFolderRequest request) {
        return ResponseEntity.ok(taskFolderMapper.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskFolderResponse> update(@PathVariable String id,
                                                     @Valid @RequestBody TaskFolderRequest request) {
        return ResponseEntity.ok(taskFolderMapper.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<TaskFolderResponse> deleteById(@PathVariable String id) {
        return ResponseEntity.ok(taskFolderMapper.deleteById(id));
    }
}
