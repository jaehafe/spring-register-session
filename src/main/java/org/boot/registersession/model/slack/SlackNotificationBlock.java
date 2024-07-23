package org.boot.registersession.model.slack;

public record SlackNotificationBlock(
        String type,
        Object text
) {
}

