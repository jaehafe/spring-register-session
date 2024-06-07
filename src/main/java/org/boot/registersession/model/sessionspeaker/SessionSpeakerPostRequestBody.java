package org.boot.registersession.model.sessionspeaker;

import jakarta.validation.constraints.NotEmpty;

public record SessionSpeakerPostRequestBody(
        @NotEmpty String company,
        @NotEmpty String name,
        @NotEmpty String description
) {

}
