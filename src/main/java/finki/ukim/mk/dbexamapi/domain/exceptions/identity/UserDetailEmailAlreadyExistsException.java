package finki.ukim.mk.dbexamapi.domain.exceptions.identity;

import finki.ukim.mk.dbexamapi.domain.exceptions.AlreadyExistsException;

public class UserDetailEmailAlreadyExistsException extends AlreadyExistsException {

    public UserDetailEmailAlreadyExistsException(String email) {
        super(String.format("User detail with email %s already exists.", email));
    }
}
