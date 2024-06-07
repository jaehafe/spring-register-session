package org.boot.registersession.model.registration;

import org.boot.registersession.model.crashsession.CrashSession;
import org.boot.registersession.model.entity.RegistrationEntity;
import org.boot.registersession.model.user.User;

public record Registration(
        Long registrationId,
        User user,
        CrashSession session
) {

    public static Registration from(RegistrationEntity registrationEntity) {
        return new Registration(
                registrationEntity.getRegistrationId(),
                User.from(registrationEntity.getUser()),
                CrashSession.from(registrationEntity.getSession())
        );
    }
}
