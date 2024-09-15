package com.sb.projects.trader.service.remote;

import com.sb.projects.trader.DTO.BrokerTokenDTO;
import com.sb.projects.trader.DTO.paytm.PaytmTokenDTO;
import com.sb.projects.trader.DTO.paytm.PaytmTokenRequestDTO;
import com.sb.projects.trader.service.BrokerTokenService;
import com.sb.projects.trader.utils.ReactiveWebClient;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

@AllArgsConstructor
public class PaytmTokenService implements BrokerTokenService<BrokerTokenDTO, PaytmTokenRequestDTO> {

    private final ReactiveWebClient<PaytmTokenDTO, PaytmTokenRequestDTO> reactiveWebClient;
    private final String uri = "accounts/v2/gettoken";

    @Override
    public Mono<BrokerTokenDTO> getToken(PaytmTokenRequestDTO body) {
        return reactiveWebClient
                    .call(uri, body)
                    .map(dto -> new BrokerTokenDTO(dto.getPaytmAccessToken()));
    }
}
