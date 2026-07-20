package finki.ukim.mk.dbexamapi.web.v1.controllers.exams;

import finki.ukim.mk.dbexamapi.web.v1.mappers.exams.ExamParticipantMapper;
import finki.ukim.mk.dbexamapi.web.v1.requests.exams.ExamParticipantRequest;
import finki.ukim.mk.dbexamapi.web.v1.responses.exams.ExamParticipantResponse;
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
@RequestMapping("/v1/exam-participants")
public class ExamParticipantController {

    private final ExamParticipantMapper examParticipantMapper;

    public ExamParticipantController(ExamParticipantMapper examParticipantMapper) {
        this.examParticipantMapper = examParticipantMapper;
    }

    @GetMapping
    public ResponseEntity<List<ExamParticipantResponse>> findAll() {
        return ResponseEntity.ok(examParticipantMapper.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExamParticipantResponse> findById(@PathVariable String id) {
        return ResponseEntity.ok(examParticipantMapper.findById(id));
    }

    @PostMapping
    public ResponseEntity<ExamParticipantResponse> create(@Valid @RequestBody ExamParticipantRequest request) {
        return ResponseEntity.ok(examParticipantMapper.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ExamParticipantResponse> update(@PathVariable String id,
                                                          @Valid @RequestBody ExamParticipantRequest request) {
        return ResponseEntity.ok(examParticipantMapper.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ExamParticipantResponse> deleteById(@PathVariable String id) {
        return ResponseEntity.ok(examParticipantMapper.deleteById(id));
    }
}
