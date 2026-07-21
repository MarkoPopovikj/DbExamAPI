package finki.ukim.mk.dbexamapi.web.v1.controllers.identity;

import finki.ukim.mk.dbexamapi.web.v1.mappers.identity.UserDetailMapper;
import finki.ukim.mk.dbexamapi.web.v1.requests.identity.UserDetailRequest;
import finki.ukim.mk.dbexamapi.web.v1.responses.identity.UserDetailResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
@RequestMapping("/v1/user-details")
@PreAuthorize("hasRole('ADMIN')")
public class UserDetailController {

    private final UserDetailMapper userDetailMapper;

    public UserDetailController(UserDetailMapper userDetailMapper) {
        this.userDetailMapper = userDetailMapper;
    }

    @GetMapping
    public ResponseEntity<List<UserDetailResponse>> findAll() {
        return ResponseEntity.ok(userDetailMapper.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDetailResponse> findById(@PathVariable String id) {
        return ResponseEntity.ok(userDetailMapper.findById(id));
    }

    @PostMapping
    public ResponseEntity<UserDetailResponse> create(@Valid @RequestBody UserDetailRequest request) {
        return ResponseEntity.ok(userDetailMapper.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDetailResponse> update(@PathVariable String id, @Valid @RequestBody UserDetailRequest request) {
        return ResponseEntity.ok(userDetailMapper.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<UserDetailResponse> deleteById(@PathVariable String id) {
        return ResponseEntity.ok(userDetailMapper.deleteById(id));
    }
}
