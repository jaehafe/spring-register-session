package org.boot.registersession.exception.registration;

import org.boot.registersession.exception.ClientErrorException;
import org.boot.registersession.model.entity.UserEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

public class RegistrationNotFoundException extends ClientErrorException {

    public RegistrationNotFoundException() {
        super(HttpStatus.NOT_FOUND, "Registration not found");
    }

    public RegistrationNotFoundException(Long registrationId, UserEntity userEntity) {
        super(HttpStatus.NOT_FOUND, "Registration with registrationId" + registrationId + userEntity.getName() + " not found");
    }
}
