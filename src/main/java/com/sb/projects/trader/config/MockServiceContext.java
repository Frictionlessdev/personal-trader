package com.sb.projects.trader.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sb.projects.trader.DTO.BrokerErrorDTO;
import com.sb.projects.trader.DTO.mock.MockOrderDataGenerator;
import com.sb.projects.trader.DTO.mock.MockStrategyDataGenerator;
import com.sb.projects.trader.DTO.paytm.*;
import com.sb.projects.trader.DTO.mock.MockStaticDataGenerator;
import com.sb.projects.trader.exceptions.BaseTraderException;
import com.sb.projects.trader.repository.OrderRepository;
import com.sb.projects.trader.repository.SecurityRepository;
import com.sb.projects.trader.repository.StrategyRepository;
import com.sb.projects.trader.service.*;
import com.sb.projects.trader.service.mock.MockStaticDataServiceImpl;
import com.sb.projects.trader.transformer.SecurityTransformer;
import io.netty.handler.logging.LogLevel;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import mockwebserver3.Dispatcher;
import mockwebserver3.MockResponse;
import mockwebserver3.RecordedRequest;
import okhttp3.Headers;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import mockwebserver3.MockWebServer;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import reactor.netty.transport.logging.AdvancedByteBufFormat;

import java.io.*;
import java.util.Map;

@Configuration
@Profile("mock")
@Slf4j
public class MockServiceContext {

    @Autowired
    MockServiceConfig mockServiceConfig;

    @Autowired
    ApplicationConfig applicationConfig;

    @Bean
    public MockStaticDataGenerator mockStaticDataGenerator() {
        return new MockStaticDataGenerator();
    }

    @Bean
    public StaticDataService mockStaticDataService(@Qualifier("mockStaticDataGenerator") MockStaticDataGenerator mockStaticDataGenerator) {
        return new MockStaticDataServiceImpl(mockStaticDataGenerator);
    }

    @Bean
    public SecurityTransformer securityTransformer() {
        return new SecurityTransformer();
    }

    @Bean
    public StaticDataService staticDataService(@Qualifier("mockStaticDataService") StaticDataService staticDataService,
                                               SecurityRepository securityRepository, SecurityTransformer securityTransformer) throws BaseTraderException {
        if (mockServiceConfig.mockStaticDataService) {
            log.info("Initializing static data mock [{}]", "MockStaticDataService");
            return staticDataService;
        } else {
            return new StaticDataServiceImpl(securityRepository, securityTransformer);
        }
    }

    @Bean
    public WebClient paytmWebClient(MockWebServer mockWebServer) throws IOException {
        String baseUrl = applicationConfig.paytmBaseUrl;
        if (mockServiceConfig.mockPaytmServices)
            baseUrl = String.format("http://localhost:%s", mockServiceConfig.mockPaytmServicesPort);

        var httpClient = HttpClient
                .create()
                .wiretap("reactor.netty.http.client.HttpClient",
                        LogLevel.INFO, AdvancedByteBufFormat.TEXTUAL);

        return WebClient.builder()
                .baseUrl(baseUrl)
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
    }

    @Bean
    @ConditionalOnProperty(name = "mocks.staticDataService", havingValue = "false")
    public StaticDataLoaderService staticDataLoaderService(@Qualifier("staticDataService") StaticDataService staticDataService) {
        return new SecuritiesStaticDataLoaderService(staticDataService);
    }

    @Bean
    @ConditionalOnProperty(name = "mocks.orderData", havingValue = "false")
    public MockOrderDataGenerator mockOrderDataGenerator(OrderRepository orderRepository){
        return new MockOrderDataGenerator(orderRepository);
    }

    @Bean
    public MockStrategyDataGenerator mockStrategyDataGenerator(StrategyRepository strategyRepository){
        return new MockStrategyDataGenerator(strategyRepository);
    }
                                                               @Bean
    public MockWebServer mockWebServer() throws IOException {
        MockWebServer mockWebServer = new MockWebServer();

        ObjectMapper objectMapper = new ObjectMapper();
        Dispatcher dispatcher = new Dispatcher() {
            @SneakyThrows
            @NotNull
            @Override
            public MockResponse dispatch(@NotNull RecordedRequest recordedRequest) throws InterruptedException {
                switch (recordedRequest.getPath()) {
                    case "/accounts/v2/gettoken":
                        if (mockServiceConfig.mockPaytmServices400Error){
                            BrokerErrorDTO expected = new BrokerErrorDTO("error",
                                    "Oops! Something went wrong.", "RS-0022");
                            return new MockResponse(400, Headers.of(Map.of("Content-Type", MediaType.APPLICATION_JSON_VALUE)),
                                    objectMapper.writeValueAsString(expected));
                        } else {
                            PaytmTokenDTO paytmTokenDTO = new PaytmTokenDTO("test access token",
                                    "test merchant id", "test channel id", "test api key",
                                    "test paytm access token", "test public access token",
                                    "test readonly access token");
                            return new MockResponse(200,
                                    Headers.of(Map.of("Content-Type", MediaType.APPLICATION_JSON_VALUE)),
                                    objectMapper.writeValueAsString(paytmTokenDTO));
                        }

                    case "/orders/v1/place/regular":
                        if (mockServiceConfig.mockPaytmServices400Error){
                            BrokerErrorDTO expected = new BrokerErrorDTO("error",
                                    "Oops! Something went wrong.", "RS-0022");
                            return new MockResponse(400, Headers.of(Map.of("Content-Type", MediaType.APPLICATION_JSON_VALUE)),
                                    objectMapper.writeValueAsString(expected));
                        } else {
                            PaytmOrderDTO paytmOrderDTO = new PaytmOrderDTO("1", "test status");
                            return new MockResponse(200,
                                    Headers.of(Map.of("Content-Type", MediaType.APPLICATION_JSON_VALUE)),
                                    objectMapper.writeValueAsString(paytmOrderDTO));
                        }
                }

                return new MockResponse(404);
            }
        };

        mockWebServer.setDispatcher(dispatcher);
        mockWebServer.start(mockServiceConfig.mockPaytmServicesPort);

        return mockWebServer;
    }
}
