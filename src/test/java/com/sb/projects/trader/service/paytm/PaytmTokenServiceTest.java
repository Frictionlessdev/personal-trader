package com.sb.projects.trader.service.paytm;

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
    BrokerTokenService<BrokerTokenDTO> brokerTokenService;

    @Autowired
    ApplicationConfig applicationConfig;

    @Test
    void givenRequestToken_makeRemoteTokenCall_Successfully(){
        Mono<BrokerTokenDTO> brokerTokenDTOMono = brokerTokenService
                .getToken();

        BrokerTokenDTO actual = brokerTokenDTOMono.block();

        assertNotNull(actual);
        assertNotNull(actual, actual.getAccessToken());
    }

}