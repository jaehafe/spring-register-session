package org.boot.registersession.exception.crashsession;

import org.boot.registersession.exception.ClientErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

public class CrashSessionNotFoundException extends ClientErrorException {

    public CrashSessionNotFoundException() {
        super(HttpStatus.NOT_FOUND, "CrashSession not found");
    }

    public CrashSessionNotFoundException(Long sessionId) {
        super(HttpStatus.NOT_FOUND, "CrashSession with sessionId" + sessionId + " not found");
    }
}
