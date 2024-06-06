package org.boot.registersession.exception.user;

import org.boot.registersession.exception.ClientErrorException;
import org.springframework.http.HttpStatus;

public class UserAlreadyExistsExceptionClient extends ClientErrorException {

    public UserAlreadyExistsExceptionClient() {
        super(HttpStatus.CONFLICT, "User already exists");
    }

    public UserAlreadyExistsExceptionClient(String username) {
        super(HttpStatus.CONFLICT, "User with username" + username + " already exists");
    }
}
