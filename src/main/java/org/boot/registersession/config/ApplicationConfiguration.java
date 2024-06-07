package org.boot.registersession.config;

import java.time.ZonedDateTime;
import java.util.Random;
import java.util.stream.IntStream;
import net.datafaker.Faker;
import org.boot.registersession.model.crashsession.CrashSessionCategory;
import org.boot.registersession.model.crashsession.CrashSessionPostRequestBody;
import org.boot.registersession.model.sessionspeaker.SessionSpeaker;
import org.boot.registersession.model.sessionspeaker.SessionSpeakerPostRequestBody;
import org.boot.registersession.model.user.UserSignUpRequestBody;
import org.boot.registersession.service.CrashSessionService;
import org.boot.registersession.service.SessionSpeakerService;
import org.boot.registersession.service.UserService;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfiguration {

    private static final Faker faker = new Faker();
    private final UserService userService;
    private final SessionSpeakerService sessionSpeakerService;
    private final CrashSessionService crashSessionService;

    public ApplicationConfiguration(UserService userService,
            SessionSpeakerService sessionSpeakerService, CrashSessionService crashSessionService) {
        this.userService = userService;
        this.sessionSpeakerService = sessionSpeakerService;
        this.crashSessionService = crashSessionService;
    }

    @Bean
    public ApplicationRunner applicationRunner() {
        return new ApplicationRunner() {
            @Override
            public void run(ApplicationArguments args) throws Exception {
                // TODO: 유저 및 세션스피커 생성
                createTestUsers();
                createTestSessionSpeakers(10);
            }
        };
    }

    private void createTestUsers() {
        userService.signUp(new UserSignUpRequestBody("adam", "1234", "jaeha", "jaeha@crash.com"));
        userService.signUp(new UserSignUpRequestBody("jay", "1234", "jay", "jay@crash.com"));
        userService.signUp(new UserSignUpRequestBody("rose", "1234", "rose", "rose@crash.com"));
        userService.signUp(new UserSignUpRequestBody("rosa", "1234", "rosa", "rosa@crash.com"));

        userService.signUp(
                new UserSignUpRequestBody(faker.name().name(), "1234", faker.name().fullName(),
                        faker.internet().emailAddress()));
        userService.signUp(
                new UserSignUpRequestBody(faker.name().name(), "1234", faker.name().fullName(),
                        faker.internet().emailAddress()));
        userService.signUp(
                new UserSignUpRequestBody(faker.name().name(), "1234", faker.name().fullName(),
                        faker.internet().emailAddress()));
    }

    private void createTestSessionSpeakers(int numberOfSpeakers) {
        var sessionSpeakers =
                IntStream.range(0, numberOfSpeakers).mapToObj(i -> createTestSessionSpeaker())
                        .toList();

        sessionSpeakers.forEach(
                sessionSpeaker -> {
                    int numberOfSessions = new Random().nextInt(4) + 1;
                    IntStream.range(0, numberOfSessions)
                            .forEach(i -> createTestCrashSession(sessionSpeaker));
                });
    }

    private SessionSpeaker createTestSessionSpeaker() {
        var name = faker.name().fullName();
        var company = faker.company().name();
        var description = faker.shakespeare().romeoAndJulietQuote();

        return sessionSpeakerService.createSessionSpeaker(
                new SessionSpeakerPostRequestBody(company, name, description));
    }

    private void createTestCrashSession(SessionSpeaker sessionSpeaker) {
        var title = faker.book().title();
        var body =
                faker.shakespeare().asYouLikeItQuote()
                        + faker.shakespeare().hamletQuote()
                        + faker.shakespeare().kingRichardIIIQuote()
                        + faker.shakespeare().romeoAndJulietQuote();

        crashSessionService.createCrashSession(
                new CrashSessionPostRequestBody(
                        title,
                        body,
                        getRandomCategory(),
                        ZonedDateTime.now().plusDays(new Random().nextInt(2) + 1),
                        sessionSpeaker.speakerId()));
    }

    private CrashSessionCategory getRandomCategory() {
        var categories = CrashSessionCategory.values();
        int randomIndex = new Random().nextInt(categories.length);
        return categories[randomIndex];
    }
}
