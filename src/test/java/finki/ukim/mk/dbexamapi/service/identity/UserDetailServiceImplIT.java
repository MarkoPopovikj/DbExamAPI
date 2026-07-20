package finki.ukim.mk.dbexamapi.service.identity;

import finki.ukim.mk.dbexamapi.domain.dtos.identity.UserDetailDto;
import finki.ukim.mk.dbexamapi.domain.dtos.identity.UserDto;
import finki.ukim.mk.dbexamapi.domain.enums.UserRole;
import finki.ukim.mk.dbexamapi.domain.exceptions.identity.UserDetailAlreadyExistsForUserException;
import finki.ukim.mk.dbexamapi.domain.exceptions.identity.UserDetailDoesNotExistException;
import finki.ukim.mk.dbexamapi.domain.exceptions.identity.UserDetailEmailAlreadyExistsException;
import finki.ukim.mk.dbexamapi.domain.exceptions.identity.UserDetailIndexAlreadyExistsException;
import finki.ukim.mk.dbexamapi.domain.models.User;
import finki.ukim.mk.dbexamapi.domain.models.UserDetail;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class UserDetailServiceImplIT {

    @Autowired
    private UserDetailService userDetailService;

    @Autowired
    private UserService userService;

    private User newUser() {
        return userService.create(new UserDto(UserRole.STUDENT));
    }

    @Test
    void create_savesUserDetailAndReturnsIt() {
        User user = newUser();

        UserDetail created = userDetailService.create(
                new UserDetailDto(user, "Marko", "Popovic", "IDX-CREATE", "create@test.com"));

        assertNotNull(created.getId());
        assertEquals(user.getId(), created.getUser().getId());
        assertEquals("Marko", created.getFirstName());
        assertEquals("Popovic", created.getLastName());
        assertEquals("IDX-CREATE", created.getIndex());
        assertEquals("create@test.com", created.getEmail());

        UserDetail found = userDetailService.findByIdNotNull(created.getId());
        assertEquals(user.getId(), found.getUser().getId());
        assertEquals("IDX-CREATE", found.getIndex());
    }

    @Test
    void create_secondDetailForSameUser_throwsUserDetailAlreadyExistsForUser() {
        User user = newUser();
        userDetailService.create(
                new UserDetailDto(user, "Marko", "Popovic", "IDX-DUP-USER-1", "dup-user-1@test.com"));

        assertThrows(UserDetailAlreadyExistsForUserException.class,
                () -> userDetailService.create(
                        new UserDetailDto(user, "Marko", "Popovic", "IDX-DUP-USER-2", "dup-user-2@test.com")));
    }

    @Test
    void create_duplicateIndex_throwsUserDetailIndexAlreadyExists() {
        userDetailService.create(
                new UserDetailDto(newUser(), "Marko", "Popovic", "IDX-DUP", "dup-index-1@test.com"));

        assertThrows(UserDetailIndexAlreadyExistsException.class,
                () -> userDetailService.create(
                        new UserDetailDto(newUser(), "Ana", "Anovska", "IDX-DUP", "dup-index-2@test.com")));
    }

    @Test
    void create_duplicateEmail_throwsUserDetailEmailAlreadyExists() {
        userDetailService.create(
                new UserDetailDto(newUser(), "Marko", "Popovic", "IDX-DUP-EMAIL-1", "dup-email@test.com"));

        assertThrows(UserDetailEmailAlreadyExistsException.class,
                () -> userDetailService.create(
                        new UserDetailDto(newUser(), "Ana", "Anovska", "IDX-DUP-EMAIL-2", "dup-email@test.com")));
    }

    @Test
    void update_keepingOwnValues_succeeds() {
        User user = newUser();
        UserDetail created = userDetailService.create(
                new UserDetailDto(user, "Marko", "Popovic", "IDX-KEEP", "keep@test.com"));

        UserDetail updated = userDetailService.update(created.getId(),
                new UserDetailDto(user, "Marko Renamed", "Popovic", "IDX-KEEP", "keep@test.com"));

        assertEquals(created.getId(), updated.getId());
        assertEquals("Marko Renamed", updated.getFirstName());
        assertEquals("IDX-KEEP", updated.getIndex());
        assertEquals("keep@test.com", updated.getEmail());
    }

    @Test
    void update_toAnotherRowsIndex_throwsUserDetailIndexAlreadyExists() {
        userDetailService.create(
                new UserDetailDto(newUser(), "Marko", "Popovic", "IDX-TAKEN", "index-taken-1@test.com"));
        User otherUser = newUser();
        UserDetail other = userDetailService.create(
                new UserDetailDto(otherUser, "Ana", "Anovska", "IDX-OTHER", "index-taken-2@test.com"));

        assertThrows(UserDetailIndexAlreadyExistsException.class,
                () -> userDetailService.update(other.getId(),
                        new UserDetailDto(otherUser, "Ana", "Anovska", "IDX-TAKEN", "index-taken-2@test.com")));
    }

    @Test
    void update_toAnotherRowsEmail_throwsUserDetailEmailAlreadyExists() {
        userDetailService.create(
                new UserDetailDto(newUser(), "Marko", "Popovic", "IDX-EMAIL-TAKEN-1", "email-taken@test.com"));
        User otherUser = newUser();
        UserDetail other = userDetailService.create(
                new UserDetailDto(otherUser, "Ana", "Anovska", "IDX-EMAIL-TAKEN-2", "email-other@test.com"));

        assertThrows(UserDetailEmailAlreadyExistsException.class,
                () -> userDetailService.update(other.getId(),
                        new UserDetailDto(otherUser, "Ana", "Anovska", "IDX-EMAIL-TAKEN-2", "email-taken@test.com")));
    }

    @Test
    void deleteById_removesUserDetailAndReturnsIt() {
        User user = newUser();
        UserDetail created = userDetailService.create(
                new UserDetailDto(user, "Marko", "Popovic", "IDX-DEL", "delete@test.com"));

        UserDetail deleted = userDetailService.deleteById(created.getId());

        assertEquals(created.getId(), deleted.getId());
        assertThrows(UserDetailDoesNotExistException.class,
                () -> userDetailService.findByIdNotNull(created.getId()));
    }
}
