package finki.ukim.mk.dbexamapi.service.identity.impl;

import finki.ukim.mk.dbexamapi.domain.dtos.identity.UserDto;
import finki.ukim.mk.dbexamapi.domain.exceptions.identity.UserDoesNotExistException;
import finki.ukim.mk.dbexamapi.domain.models.identity.User;
import finki.ukim.mk.dbexamapi.repository.identity.UserRepository;
import finki.ukim.mk.dbexamapi.service.identity.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Optional<User> findById(String id) {
        return userRepository.findById(id);
    }

    @Override
    public User findByIdNotNull(String id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserDoesNotExistException(id));
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    @Transactional
    public User create(UserDto userDto) {
        User user = new User();
        user.setRole(userDto.role());

        User saved = userRepository.save(user);
        log.info("Created user with id: {}", saved.getId());
        return saved;
    }

    @Override
    @Transactional
    public User update(String id, UserDto userDto) {
        User user = findByIdNotNull(id);

        user.setRole(userDto.role());

        User saved = userRepository.save(user);
        log.info("Updated user with id: {}", saved.getId());
        return saved;
    }

    @Override
    @Transactional
    public User deleteById(String id) {
        User user = findByIdNotNull(id);
        userRepository.delete(user);
        log.info("Deleted user with id: {}", id);
        return user;
    }
}
