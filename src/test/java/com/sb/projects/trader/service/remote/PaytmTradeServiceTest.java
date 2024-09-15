package com.sb.projects.trader.service.remote;

import com.sb.projects.trader.DTO.OrderDTO;
import com.sb.projects.trader.DTO.paytm.PaytmOrderDTO;
import com.sb.projects.trader.DTO.paytm.PaytmOrderRequestDTO;
import com.sb.projects.trader.DTO.paytm.PaytmTokenDTO;
import com.sb.projects.trader.TraderApplicationTests;
import com.sb.projects.trader.enums.Exchange;
import com.sb.projects.trader.service.BrokerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.*;

class PaytmTradeServiceTest extends TraderApplicationTests {

    @Autowired
    BrokerService<PaytmOrderDTO, PaytmOrderRequestDTO> paytmBrokerService;

    @Test
    void givenOrder_whenPaytmService_thenSubmitOrder(){
        PaytmOrderRequestDTO paytmOrderRequestDTO = PaytmOrderRequestDTO.builder()
                .transactionType("B")
                .exchange(Exchange.NSE)
                .segment("E")
                .product("C")
                .securityId("10176")
                .quantity(1)
                .validity("DAY")
                .orderType("MKT")
                .price(0)
                .source("N")
                .offMktFlag("true")
                .build();

        Mono<PaytmOrderDTO> paytmOrderDTOMono = paytmBrokerService
                .submitOrder(paytmOrderRequestDTO);

        PaytmOrderDTO paytmOrderDTO = paytmOrderDTOMono.block();

        assertNotNull(paytmOrderDTO);
    }
}