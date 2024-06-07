package org.boot.registersession.exception.registration;

import org.boot.registersession.exception.ClientErrorException;
import org.boot.registersession.model.entity.UserEntity;
import org.springframework.http.HttpStatus;

public class RegistrationAlreadyExistsException extends ClientErrorException {

    public RegistrationAlreadyExistsException() {
        super(HttpStatus.CONFLICT, "Registration already exists");
    }

    public RegistrationAlreadyExistsException(Long registrationId, UserEntity userEntity) {
        super(HttpStatus.CONFLICT,
                "Registration with registrationId" + registrationId + " and name "
                        + userEntity.getName() + " already exists");
    }
}
