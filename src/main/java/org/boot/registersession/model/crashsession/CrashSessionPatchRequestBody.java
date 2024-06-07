package org.boot.registersession.model.crashsession;

import jakarta.validation.constraints.NotEmpty;
import java.time.ZonedDateTime;

public record CrashSessionPatchRequestBody(

        String title,

        String body,

        CrashSessionCategory category,

        ZonedDateTime dateTime,

        Long speakerId
) {

}
