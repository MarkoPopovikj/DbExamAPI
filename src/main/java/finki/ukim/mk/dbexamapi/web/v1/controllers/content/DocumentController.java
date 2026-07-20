package finki.ukim.mk.dbexamapi.web.v1.controllers.content;

import finki.ukim.mk.dbexamapi.web.v1.mappers.content.DocumentMapper;
import finki.ukim.mk.dbexamapi.web.v1.requests.content.DocumentRequest;
import finki.ukim.mk.dbexamapi.web.v1.responses.content.DocumentResponse;
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
@RequestMapping("/v1/documents")
public class DocumentController {

    private final DocumentMapper documentMapper;

    public DocumentController(DocumentMapper documentMapper) {
        this.documentMapper = documentMapper;
    }

    @GetMapping
    public ResponseEntity<List<DocumentResponse>> findAll() {
        return ResponseEntity.ok(documentMapper.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DocumentResponse> findById(@PathVariable String id) {
        return ResponseEntity.ok(documentMapper.findById(id));
    }

    @PostMapping
    public ResponseEntity<DocumentResponse> create(@Valid @RequestBody DocumentRequest request) {
        return ResponseEntity.ok(documentMapper.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DocumentResponse> update(@PathVariable String id,
                                                   @Valid @RequestBody DocumentRequest request) {
        return ResponseEntity.ok(documentMapper.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<DocumentResponse> deleteById(@PathVariable String id) {
        return ResponseEntity.ok(documentMapper.deleteById(id));
    }
}
