package finki.ukim.mk.dbexamapi.service.identity;

import finki.ukim.mk.dbexamapi.domain.dtos.identity.UserDto;
import finki.ukim.mk.dbexamapi.domain.enums.UserRole;
import finki.ukim.mk.dbexamapi.domain.exceptions.identity.UserDoesNotExistException;
import finki.ukim.mk.dbexamapi.domain.models.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class UserServiceImplIT {

    @Autowired
    private UserService userService;

    @Test
    void create_savesUserAndReturnsIt() {
        User created = userService.create(new UserDto(UserRole.STUDENT));

        assertNotNull(created.getId());
        assertEquals(UserRole.STUDENT, created.getRole());

        User found = userService.findByIdNotNull(created.getId());
        assertEquals(UserRole.STUDENT, found.getRole());
    }

    @Test
    void update_replacesRole() {
        User created = userService.create(new UserDto(UserRole.STUDENT));

        User updated = userService.update(created.getId(), new UserDto(UserRole.ADMIN));

        assertEquals(created.getId(), updated.getId());
        assertEquals(UserRole.ADMIN, updated.getRole());

        User found = userService.findByIdNotNull(created.getId());
        assertEquals(UserRole.ADMIN, found.getRole());
    }

    @Test
    void deleteById_removesUserAndReturnsIt() {
        User created = userService.create(new UserDto(UserRole.STUDENT));

        User deleted = userService.deleteById(created.getId());

        assertEquals(created.getId(), deleted.getId());
        assertThrows(UserDoesNotExistException.class,
                () -> userService.findByIdNotNull(created.getId()));
    }

    @Test
    void findByIdNotNull_missingId_throwsUserDoesNotExist() {
        assertThrows(UserDoesNotExistException.class,
                () -> userService.findByIdNotNull("missing-user-id"));
    }
}
