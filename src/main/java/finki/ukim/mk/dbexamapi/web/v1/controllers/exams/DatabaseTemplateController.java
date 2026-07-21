package finki.ukim.mk.dbexamapi.web.v1.controllers.exams;

import finki.ukim.mk.dbexamapi.web.v1.mappers.exams.DatabaseTemplateMapper;
import finki.ukim.mk.dbexamapi.web.v1.requests.exams.DatabaseTemplateRequest;
import finki.ukim.mk.dbexamapi.web.v1.responses.exams.DatabaseTemplateResponse;
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
@RequestMapping("/v1/database-templates")
public class DatabaseTemplateController {

    private final DatabaseTemplateMapper databaseTemplateMapper;

    public DatabaseTemplateController(DatabaseTemplateMapper databaseTemplateMapper) {
        this.databaseTemplateMapper = databaseTemplateMapper;
    }

    @GetMapping
    public ResponseEntity<List<DatabaseTemplateResponse>> findAll() {
        return ResponseEntity.ok(databaseTemplateMapper.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DatabaseTemplateResponse> findById(@PathVariable String id) {
        return ResponseEntity.ok(databaseTemplateMapper.findById(id));
    }

    @PostMapping
    public ResponseEntity<DatabaseTemplateResponse> create(@Valid @RequestBody DatabaseTemplateRequest request) {
        return ResponseEntity.ok(databaseTemplateMapper.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DatabaseTemplateResponse> update(@PathVariable String id,
                                                           @Valid @RequestBody DatabaseTemplateRequest request) {
        return ResponseEntity.ok(databaseTemplateMapper.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<DatabaseTemplateResponse> retireById(@PathVariable String id) {
        return ResponseEntity.ok(databaseTemplateMapper.retireById(id));
    }

    @PostMapping("/{id}/activate")
    public ResponseEntity<DatabaseTemplateResponse> activateById(@PathVariable String id) {
        return ResponseEntity.ok(databaseTemplateMapper.activateById(id));
    }
}
