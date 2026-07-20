package finki.ukim.mk.dbexamapi.web.v1.controllers.identity;

import finki.ukim.mk.dbexamapi.web.v1.mappers.identity.UserMapper;
import finki.ukim.mk.dbexamapi.web.v1.requests.identity.UserRequest;
import finki.ukim.mk.dbexamapi.web.v1.responses.identity.UserResponse;
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
@RequestMapping("/v1/users")
public class UserController {

    private final UserMapper userMapper;

    public UserController(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @GetMapping
    public ResponseEntity<List<UserResponse>> findAll() {
        return ResponseEntity.ok(userMapper.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> findById(@PathVariable String id) {
        return ResponseEntity.ok(userMapper.findById(id));
    }

    @PostMapping
    public ResponseEntity<UserResponse> create(@Valid @RequestBody UserRequest request) {
        return ResponseEntity.ok(userMapper.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> update(@PathVariable String id, @Valid @RequestBody UserRequest request) {
        return ResponseEntity.ok(userMapper.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<UserResponse> deleteById(@PathVariable String id) {
        return ResponseEntity.ok(userMapper.deleteById(id));
    }
}
