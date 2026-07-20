package finki.ukim.mk.dbexamapi.web.v1.controllers.content;

import finki.ukim.mk.dbexamapi.web.v1.mappers.content.ExamFolderMapper;
import finki.ukim.mk.dbexamapi.web.v1.requests.content.ExamFolderRequest;
import finki.ukim.mk.dbexamapi.web.v1.responses.content.ExamFolderResponse;
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
@RequestMapping("/v1/exam-folders")
public class ExamFolderController {

    private final ExamFolderMapper examFolderMapper;

    public ExamFolderController(ExamFolderMapper examFolderMapper) {
        this.examFolderMapper = examFolderMapper;
    }

    @GetMapping
    public ResponseEntity<List<ExamFolderResponse>> findAll() {
        return ResponseEntity.ok(examFolderMapper.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExamFolderResponse> findById(@PathVariable String id) {
        return ResponseEntity.ok(examFolderMapper.findById(id));
    }

    @PostMapping
    public ResponseEntity<ExamFolderResponse> create(@Valid @RequestBody ExamFolderRequest request) {
        return ResponseEntity.ok(examFolderMapper.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ExamFolderResponse> update(@PathVariable String id,
                                                     @Valid @RequestBody ExamFolderRequest request) {
        return ResponseEntity.ok(examFolderMapper.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ExamFolderResponse> deleteById(@PathVariable String id) {
        return ResponseEntity.ok(examFolderMapper.deleteById(id));
    }
}
