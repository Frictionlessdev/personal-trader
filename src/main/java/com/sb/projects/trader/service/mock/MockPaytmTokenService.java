package com.sb.projects.trader.service.mock;

import com.sb.projects.trader.DTO.BrokerTokenDTO;
import com.sb.projects.trader.DTO.paytm.PaytmTokenDTO;
import com.sb.projects.trader.DTO.paytm.PaytmTokenRequestDTO;
import com.sb.projects.trader.service.BrokerTokenService;
import com.sb.projects.trader.service.paytm.PaytmTokenService;
import com.sb.projects.trader.utils.ReactiveWebClient;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
public class MockPaytmTokenService implements BrokerTokenService<BrokerTokenDTO, String> {

    private final String apiKey;
    private final String apiSecret;
    private final ReactiveWebClient<PaytmTokenDTO, PaytmTokenRequestDTO> reactiveWebClient;
    private final String uri = "accounts/v2/gettoken";

    @Override
    public Mono<BrokerTokenDTO> getToken(String requestToken) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Host","developer.paytmmoney.com");

        return reactiveWebClient
                .call(uri, headers, PaytmTokenRequestDTO.builder()
                        .apiKey(apiKey)
                        .apiSecret(apiSecret)
                        .requestToken(requestToken).build())
                .map(dto -> new BrokerTokenDTO(dto.getPaytmAccessToken()));
    }
}
