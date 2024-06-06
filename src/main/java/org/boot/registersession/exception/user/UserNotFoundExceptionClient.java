package org.boot.registersession.exception.user;

import org.boot.registersession.exception.ClientErrorException;
import org.springframework.http.HttpStatus;

public class UserNotFoundExceptionClient extends ClientErrorException {

    public UserNotFoundExceptionClient() {
        super(HttpStatus.NOT_FOUND, "User not found");
    }

    public UserNotFoundExceptionClient(String username) {
        super(HttpStatus.NOT_FOUND, "User with username" + username + " not found");
    }
}
