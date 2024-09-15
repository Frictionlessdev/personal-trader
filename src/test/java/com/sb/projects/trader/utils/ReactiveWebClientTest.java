package com.sb.projects.trader.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sb.projects.trader.DTO.paytm.PaytmTokenDTO;
import com.sb.projects.trader.DTO.paytm.PaytmTokenRequestDTO;
import io.netty.handler.logging.LogLevel;
import mockwebserver3.MockResponse;
import mockwebserver3.MockWebServer;
import okhttp3.Headers;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;
import reactor.netty.transport.logging.AdvancedByteBufFormat;

import java.io.IOException;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;

@ExtendWith(SpringExtension.class)
class ReactiveWebClientTest {

    public static MockWebServer mockWebServer;

    ReactiveWebClient<PaytmTokenDTO, PaytmTokenRequestDTO> reactiveWebClient;
    ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();

        var httpClient = HttpClient
                .create()
                .wiretap("reactor.netty.http.client.HttpClient",
                        LogLevel.DEBUG, AdvancedByteBufFormat.TEXTUAL);

        var webClient = WebClient.builder()
                .baseUrl(String.format("http://localhost:%s", mockWebServer.getPort()))
                .defaultHeader("Accept", MediaType.APPLICATION_JSON_VALUE,
                        "Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();

        reactiveWebClient = new ReactiveWebClient<>(webClient, PaytmTokenDTO.class, PaytmTokenRequestDTO.class);
    }

    @Test
    void makeWebClientCallSuccessfully() throws JsonProcessingException {
        PaytmTokenDTO expected = new PaytmTokenDTO("test access token",
                "test merchant id", "test channel id", "test api key",
                "test paytm access token", "test public access token",
                "test readonly access token");

        PaytmTokenRequestDTO paytmTokenRequestDTO = PaytmTokenRequestDTO.builder()
                .apiKey("Test key")
                .apiSecret("Test secret")
                .requestToken("test paytm access token")
                .build();

        mockWebServer.enqueue(new MockResponse(200, Headers.of(Map.of("Content-Type", MediaType.APPLICATION_JSON_VALUE)),
                objectMapper.writeValueAsString(expected)));

        Mono<PaytmTokenDTO> paytmTokenDTOMono = reactiveWebClient.call("", paytmTokenRequestDTO);
        PaytmTokenDTO actual = paytmTokenDTOMono.block();

        assertThat(expected, Matchers.samePropertyValuesAs(actual));
    }
}