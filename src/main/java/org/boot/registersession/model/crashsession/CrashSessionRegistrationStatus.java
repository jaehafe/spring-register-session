package org.boot.registersession.model.crashsession;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record CrashSessionRegistrationStatus(
        Long sessionId,
        boolean isRegistered,
        Long registrationId
) {

}
