package org.boot.registersession.model.crashsession;

import java.time.ZonedDateTime;
import org.boot.registersession.model.entity.CrashSessionEntity;
import org.boot.registersession.model.sessionspeaker.SessionSpeaker;

public record CrashSession(
        Long sessionId,
        String title,
        String body,
        CrashSessionCategory category,
        ZonedDateTime dateTime,
        SessionSpeaker speaker
) {

    public static CrashSession from(CrashSessionEntity crashSessionEntity) {
        return new CrashSession(
                crashSessionEntity.getSessionId(),
                crashSessionEntity.getTitle(),
                crashSessionEntity.getBody(),
                crashSessionEntity.getCategory(),
                crashSessionEntity.getDateTime(),
                SessionSpeaker.from(crashSessionEntity.getSpeaker())
        );
    }
}
