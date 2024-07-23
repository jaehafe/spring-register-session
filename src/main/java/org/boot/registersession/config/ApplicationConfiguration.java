package org.boot.registersession.config;

import java.nio.charset.StandardCharsets;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Random;
import java.util.stream.IntStream;
import net.datafaker.Faker;
import org.boot.registersession.exchange.ExchangeResponse;
import org.boot.registersession.model.coinbase.PriceResponse;
import org.boot.registersession.model.crashsession.CrashSessionCategory;
import org.boot.registersession.model.crashsession.CrashSessionPostRequestBody;
import org.boot.registersession.model.sessionspeaker.SessionSpeaker;
import org.boot.registersession.model.sessionspeaker.SessionSpeakerPostRequestBody;
import org.boot.registersession.model.user.UserSignUpRequestBody;
import org.boot.registersession.service.CrashSessionService;
import org.boot.registersession.service.SessionSpeakerService;
import org.boot.registersession.service.SlackService;
import org.boot.registersession.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.client.RestClient;

@Configuration
public class ApplicationConfiguration {

    private static final RestClient restClient = RestClient.create();
    private static final Logger logger = LoggerFactory.getLogger(ApplicationConfiguration.class);
    private static final Faker faker = new Faker();
    private final UserService userService;
    private final SessionSpeakerService sessionSpeakerService;
    private final CrashSessionService crashSessionService;
    private final SlackService slackService;

    public ApplicationConfiguration(UserService userService,
            SessionSpeakerService sessionSpeakerService, CrashSessionService crashSessionService, SlackService slackService) {
        this.userService = userService;
        this.sessionSpeakerService = sessionSpeakerService;
        this.crashSessionService = crashSessionService;
        this.slackService = slackService;
    }

    @Bean
    public ApplicationRunner applicationRunner() {
        return new ApplicationRunner() {
            @Override
            public void run(ApplicationArguments args) throws Exception {
//                slackService.sendSlackMessage();

                // TODO: 유저 및 세션스피커 생성
                createTestUsers();
                createTestSessionSpeakers(10);

//                // TODO: Bitcoin USD 가격 조회
//                var bitcoinUsdPrice = getBitcoinUsdPrice();
//                // TODO: USD to KRW 환율 조회
//                var usdToKrwExchangeRate = getUsdToKrwExchangeRate();
//                // TODO: Bitcoin KRW 가격 계산
//                var koreanPremium = 1.1;
//                var bitcoinKrwPrice = bitcoinUsdPrice * usdToKrwExchangeRate * koreanPremium;
//
//                logger.info(String.format("BTC KRW: %.2f", bitcoinKrwPrice));
            }
        };
    }

    private Double getBitcoinUsdPrice() {
        var response = restClient
                .get()
                .uri("https://api.coinbase.com/v2/prices/BTC-USD/buy")
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, (req, res) -> {
                    // TODO: 클라이언트 에러 예외 처리
                    // throw new MyCustomException();
                    logger.error(new String(res.getBody().readAllBytes(), StandardCharsets.UTF_8));
                })
                .body(PriceResponse.class);

        logger.info(String.valueOf(response));
        assert response != null;
        return Double.parseDouble(response.data().amount());
    }

    private Double getUsdToKrwExchangeRate() {
        var response =
                restClient
                        .get()
                        .uri(
                                "https://www.koreaexim.go.kr/site/program/financial/exchangeJSON?authkey=rEo29Gjp9DQJAiEHdOXVTijxnKqRg0YH&searchdate=20240312&data=AP01")
                        .retrieve()
                        .onStatus(
                                HttpStatusCode::is4xxClientError,
                                (req, res) -> {
                                    logger.error(new String(res.getBody().readAllBytes(),
                                            StandardCharsets.UTF_8));
                                })
                        .body(ExchangeResponse[].class);

        assert response != null;

        var usdToKrwExchangeRate =
                Arrays.stream(response)
                        .filter(exchangeResponse -> exchangeResponse.cur_unit().equals("USD"))
                        .findFirst()
                        .orElseThrow();

        return Double.parseDouble(usdToKrwExchangeRate.deal_bas_r().replace(",", ""));
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
