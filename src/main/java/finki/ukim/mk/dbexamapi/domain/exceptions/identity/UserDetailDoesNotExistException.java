package finki.ukim.mk.dbexamapi.domain.exceptions.identity;

import finki.ukim.mk.dbexamapi.domain.exceptions.NotFoundException;

public class UserDetailDoesNotExistException extends NotFoundException {

    public UserDetailDoesNotExistException(String id) {
        super(String.format("User detail with id %s does not exist.", id));
    }
}
