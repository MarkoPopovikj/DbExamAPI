package finki.ukim.mk.dbexamapi.service.identity;

import finki.ukim.mk.dbexamapi.domain.dtos.identity.UserDto;
import finki.ukim.mk.dbexamapi.domain.models.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    Optional<User> findById(String id);

    User findByIdNotNull(String id);

    List<User> findAll();

    User create(UserDto userDto);

    User update(String id, UserDto userDto);

    User deleteById(String id);
}
