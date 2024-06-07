package org.boot.registersession.exception.sessionspeaker;

import org.boot.registersession.exception.ClientErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

public class SessionSpeakerNotFoundException extends ClientErrorException {

    private static final Logger logger = LoggerFactory.getLogger(SessionSpeakerNotFoundException.class);


    public SessionSpeakerNotFoundException() {
        super(HttpStatus.NOT_FOUND, "SessionSpeaker not found");
    }

    public SessionSpeakerNotFoundException(Long speakerId) {
        super(HttpStatus.NOT_FOUND, "SessionSpeaker with speakerId" + speakerId + " not found");
        logger.error("SessionSpeaker with speakerId {} not found", speakerId);
    }
}
