package finki.ukim.mk.dbexamapi.domain.exceptions.identity;

import finki.ukim.mk.dbexamapi.domain.exceptions.AlreadyExistsException;

public class UserDetailIndexAlreadyExistsException extends AlreadyExistsException {

    public UserDetailIndexAlreadyExistsException(String index) {
        super(String.format("User detail with index %s already exists.", index));
    }
}
