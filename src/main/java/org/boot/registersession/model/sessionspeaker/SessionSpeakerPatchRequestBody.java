package org.boot.registersession.model.sessionspeaker;

import jakarta.validation.constraints.NotEmpty;

public record SessionSpeakerPatchRequestBody(
        String company,
        String name,
        String description
) {

}
