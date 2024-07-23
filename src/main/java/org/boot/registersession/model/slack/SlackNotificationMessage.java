package org.boot.registersession.model.slack;

import java.util.List;

public record SlackNotificationMessage(
        List<SlackNotificationBlock> blocks
) {
}
