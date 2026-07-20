package finki.ukim.mk.dbexamapi.web.v1.controllers.content;

import finki.ukim.mk.dbexamapi.web.v1.mappers.content.FolderMapper;
import finki.ukim.mk.dbexamapi.web.v1.requests.content.FolderRequest;
import finki.ukim.mk.dbexamapi.web.v1.responses.content.FolderResponse;
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
@RequestMapping("/v1/folders")
public class FolderController {

    private final FolderMapper folderMapper;

    public FolderController(FolderMapper folderMapper) {
        this.folderMapper = folderMapper;
    }

    @GetMapping
    public ResponseEntity<List<FolderResponse>> findAll() {
        return ResponseEntity.ok(folderMapper.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<FolderResponse> findById(@PathVariable String id) {
        return ResponseEntity.ok(folderMapper.findById(id));
    }

    @PostMapping
    public ResponseEntity<FolderResponse> create(@Valid @RequestBody FolderRequest request) {
        return ResponseEntity.ok(folderMapper.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<FolderResponse> update(@PathVariable String id,
                                                 @Valid @RequestBody FolderRequest request) {
        return ResponseEntity.ok(folderMapper.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<FolderResponse> deleteById(@PathVariable String id) {
        return ResponseEntity.ok(folderMapper.deleteById(id));
    }
}
