package org.boot.registersession.service;

import org.boot.registersession.model.registration.Registration;
import org.boot.registersession.model.slack.SlackNotificationBlock;
import org.boot.registersession.model.slack.SlackNotificationMessage;
import org.boot.registersession.model.slack.SlackNotificationText;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

@Service
public class SlackService {

    private static final RestClient restClient = RestClient.create();
    private static final Logger logger = LoggerFactory.getLogger(SlackService.class);
    private final String webhookUri;

    public SlackService(@Value("${slack.webhook-uri}") String webhookUri) {
        this.webhookUri = webhookUri;
    }

    public void sendSlackMessage(Registration registration) {
        var linkText = getRegistrationPageLinkText(registration);

        var slackNotificationMessage = new SlackNotificationMessage(
                List.of(new SlackNotificationBlock(
                                "section",
                                new SlackNotificationText(
                                        "mrkdwn", linkText
                                )
                        )
                ));

        var response = restClient
                .post()
                .uri(webhookUri)
                .body(slackNotificationMessage)
                .retrieve()
                .body(String.class);

        logger.info("slack response::: {}", response);
    }

    private String getRegistrationPageLinkText(Registration registration) {
        var baseLink = "https://github.com/jaehafe";
        var registrationId = registration.registrationId();
        var username = registration.user().username();
        var sessionId = registration.session().sessionId();
        var link = baseLink + registrationId + username + sessionId;
        return ":collision: *CRASH* <" + link + ">";
    }
}
