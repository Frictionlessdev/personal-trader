package com.sb.projects.trader.service.remote;

import com.sb.projects.trader.DTO.BrokerTokenDTO;
import com.sb.projects.trader.DTO.paytm.PaytmTokenRequestDTO;
import com.sb.projects.trader.TraderApplicationTests;
import com.sb.projects.trader.config.ApplicationConfig;
import com.sb.projects.trader.service.BrokerTokenService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.*;

class PaytmTokenServiceTest extends TraderApplicationTests {

    /*@Autowired
    PaytmTokenService paytmTokenService;*/

    @Autowired
    BrokerTokenService<BrokerTokenDTO, PaytmTokenRequestDTO> brokerTokenService;

    @Autowired
    ApplicationConfig applicationConfig;

    private String requestToken = "9a72b7b88cab41298f776a07a3f32f11";

    @Test
    void givenRequestToken_makeRemoteTokenCall_Successfully(){
        Mono<BrokerTokenDTO> brokerTokenDTOMono = brokerTokenService
                .getToken(PaytmTokenRequestDTO.builder()
                        .apiKey(applicationConfig.apiKey)
                        .apiSecret(applicationConfig.apiSecret)
                        .requestToken(requestToken).build());

        BrokerTokenDTO actual = brokerTokenDTOMono.block();

        assertNotNull(actual);
        assertNotNull(actual, actual.getAccessToken());
    }

}