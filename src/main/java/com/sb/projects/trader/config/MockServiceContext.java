package com.sb.projects.trader.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sb.projects.trader.DTO.BrokerTokenDTO;
import com.sb.projects.trader.DTO.mock.MockOrderDataGenerator;
import com.sb.projects.trader.DTO.paytm.PaytmOrderDTO;
import com.sb.projects.trader.DTO.paytm.PaytmOrderRequestDTO;
import com.sb.projects.trader.DTO.paytm.PaytmTokenDTO;
import com.sb.projects.trader.DTO.mock.MockStaticDataGenerator;
import com.sb.projects.trader.DTO.paytm.PaytmTokenRequestDTO;
import com.sb.projects.trader.entity.Order;
import com.sb.projects.trader.exceptions.BaseTraderException;
import com.sb.projects.trader.repository.OrderRepository;
import com.sb.projects.trader.repository.SecurityRepository;
import com.sb.projects.trader.service.*;
import com.sb.projects.trader.service.mock.MockPaytmTokenService;
import com.sb.projects.trader.service.mock.MockPaytmTradeService;
import com.sb.projects.trader.service.mock.MockStaticDataServiceImpl;
import com.sb.projects.trader.service.paytm.PaytmOrderProcessingService;
import com.sb.projects.trader.service.paytm.PaytmTokenService;
import com.sb.projects.trader.service.paytm.PaytmTradeService;
import com.sb.projects.trader.transformer.BaseEntityTransformer;
import com.sb.projects.trader.transformer.OrderTransformer;
import com.sb.projects.trader.transformer.SecurityTransformer;
import com.sb.projects.trader.utils.ReactiveWebClient;
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
    public WebClient paytmWebClient() throws IOException {
        setUpMockBrokerServer(9000);

        String baseUrl = applicationConfig.paytmBaseUrl;
        if (mockServiceConfig.mockPaytmServices) {
            baseUrl = String.format("http://localhost:%s", 9000);
        }

        var httpClient = HttpClient
                .create()
                .wiretap("reactor.netty.http.client.HttpClient",
                        LogLevel.DEBUG, AdvancedByteBufFormat.TEXTUAL);

        return WebClient.builder()
                .baseUrl(baseUrl)
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
    }

    @Bean
    public ReactiveWebClient<PaytmOrderDTO, PaytmOrderRequestDTO> paytmTradeDTOReactiveWebClient(@Qualifier("paytmWebClient") WebClient paytmWebClient) {
        return new ReactiveWebClient<>(paytmWebClient, PaytmOrderDTO.class, PaytmOrderRequestDTO.class);
    }

    @Bean
    public ReactiveWebClient<PaytmTokenDTO, PaytmTokenRequestDTO> paytmTokenDTOReactiveWebClient(@Qualifier("paytmWebClient") WebClient paytmWebClient) {
        return new ReactiveWebClient<>(paytmWebClient, PaytmTokenDTO.class, PaytmTokenRequestDTO.class);
    }

    @Bean
    public BrokerService<PaytmOrderDTO, PaytmOrderRequestDTO> paytmBrokerService(
            @Qualifier("paytmTradeDTOReactiveWebClient") ReactiveWebClient reactiveWebClient,
            BrokerTokenService paytmTokenService) {
        if (mockServiceConfig.mockPaytmServices) {
            log.info("Initializing Trade mock [{}]", "MockPaytmTradeService");
            return new MockPaytmTradeService(paytmTokenService, reactiveWebClient);
        } else return new PaytmTradeService(paytmTokenService,reactiveWebClient);
    }

    @Bean
    public BrokerTokenService<BrokerTokenDTO, String> paytmTokenService(
            @Qualifier("paytmTokenDTOReactiveWebClient") ReactiveWebClient reactiveWebClient) throws BaseTraderException {
        if (mockServiceConfig.mockPaytmServices) {
            log.info("Initializing token mock [{}]", "MockPaytmTokenService");
            return new MockPaytmTokenService(applicationConfig.apiKey, applicationConfig.apiSecret, reactiveWebClient);
        } else return new PaytmTokenService(applicationConfig.apiKey, applicationConfig.apiSecret, reactiveWebClient);
    }

    @Bean
    public OrderTransformer orderTransformer(){
        return new OrderTransformer();
    }

    @Bean
    public OrderService orderService(OrderRepository orderRepository, OrderTransformer orderTransformer){
        return new OrderServiceImpl(orderRepository, orderTransformer);
    }

    @Bean
    public BaseEntityTransformer<Order, PaytmOrderRequestDTO> paytmOrderRequestDTOTransformer(){
        return new BaseEntityTransformer<Order, PaytmOrderRequestDTO>() {};
    }

    @Bean
    public OrderProcessingService orderProcessingService(OrderService orderService,
                                                         @Qualifier("paytmBrokerService") BrokerService<PaytmOrderDTO, PaytmOrderRequestDTO> paytmOrderService,
                                                         @Qualifier("paytmOrderRequestDTOTransformer") BaseEntityTransformer<Order, PaytmOrderRequestDTO> baseEntityTransformer){
        return new PaytmOrderProcessingService(applicationConfig.processorInterval,
                applicationConfig.processorInitialDelay,
                orderService, paytmOrderService, baseEntityTransformer);
    }

    @Bean
    @ConditionalOnProperty(name = "mocks.staticDataService", havingValue = "false")
    public StaticDataLoaderService staticDataLoaderService(@Qualifier("staticDataService") StaticDataService staticDataService) {
        return new SecuritiesStaticDataLoaderService(staticDataService);
    }

    @Bean
    public MockOrderDataGenerator mockOrderDataGenerator(OrderRepository orderRepository){
        return new MockOrderDataGenerator(orderRepository);
    }

    public void setUpMockBrokerServer(int port) throws IOException {
        MockWebServer mockWebServer = new MockWebServer();

        ObjectMapper objectMapper = new ObjectMapper();
        Dispatcher dispatcher = new Dispatcher() {
            @SneakyThrows
            @NotNull
            @Override
            public MockResponse dispatch(@NotNull RecordedRequest recordedRequest) throws InterruptedException {
                switch (recordedRequest.getPath()) {
                    case "/accounts/v2/gettoken":
                        PaytmTokenDTO paytmTokenDTO = new PaytmTokenDTO("test access token",
                                "test merchant id", "test channel id", "test api key",
                                "test paytm access token", "test public access token",
                                "test readonly access token");
                        return new MockResponse(200,
                                Headers.of(Map.of("Content-Type", MediaType.APPLICATION_JSON_VALUE)),
                                objectMapper.writeValueAsString(paytmTokenDTO));

                    case "/orders/v1/place/regular":
                        PaytmOrderDTO paytmOrderDTO = new PaytmOrderDTO("1", "test status");
                        return new MockResponse(200,
                                Headers.of(Map.of("Content-Type", MediaType.APPLICATION_JSON_VALUE)),
                                objectMapper.writeValueAsString(paytmOrderDTO));
                }

                return new MockResponse(404);
            }
        };

        mockWebServer.setDispatcher(dispatcher);
        mockWebServer.start(port);
    }
}
