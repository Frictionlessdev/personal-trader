package com.sb.projects.trader.service.remote;

import com.sb.projects.trader.DTO.paytm.PaytmOrderDTO;
import com.sb.projects.trader.DTO.paytm.PaytmOrderRequestDTO;
import com.sb.projects.trader.service.BrokerService;
import com.sb.projects.trader.utils.ReactiveWebClient;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

@AllArgsConstructor
public class PaytmTradeService implements BrokerService<PaytmOrderDTO, PaytmOrderRequestDTO> {

    private final ReactiveWebClient<PaytmOrderDTO, PaytmOrderRequestDTO> reactiveWebClient;
    private final String uri = "orders/v1/place/regular";

    @Override
    public Mono<PaytmOrderDTO> submitOrder(PaytmOrderRequestDTO paytmOrderRequestDTO) {
        return reactiveWebClient
                .call(uri, paytmOrderRequestDTO)
                .map(dto -> new PaytmOrderDTO(dto.getId()));
    }
}
