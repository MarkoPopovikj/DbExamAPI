package finki.ukim.mk.dbexamapi.domain.exceptions.identity;

import finki.ukim.mk.dbexamapi.domain.exceptions.NotFoundException;

public class UserDoesNotExistException extends NotFoundException {

    public UserDoesNotExistException(String id) {
        super(String.format("User with id %s does not exist.", id));
    }
}
