package finki.ukim.mk.dbexamapi.domain.exceptions.identity;

import finki.ukim.mk.dbexamapi.domain.exceptions.AlreadyExistsException;

public class UserDetailAlreadyExistsForUserException extends AlreadyExistsException {

    public UserDetailAlreadyExistsForUserException(String userId) {
        super(String.format("User detail for user with id %s already exists.", userId));
    }
}
