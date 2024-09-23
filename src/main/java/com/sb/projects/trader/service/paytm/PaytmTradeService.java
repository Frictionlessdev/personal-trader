package com.sb.projects.trader.service.paytm;

import com.sb.projects.trader.DTO.BrokerTokenDTO;
import com.sb.projects.trader.DTO.paytm.PaytmOrderDTO;
import com.sb.projects.trader.DTO.paytm.PaytmOrderRequestDTO;
import com.sb.projects.trader.DTO.paytm.PaytmTokenDTO;
import com.sb.projects.trader.enums.ErrorCode;
import com.sb.projects.trader.exceptions.BaseTraderException;
import com.sb.projects.trader.service.BrokerService;
import com.sb.projects.trader.service.BrokerTokenService;
import com.sb.projects.trader.utils.ReactiveWebClient;
import lombok.AllArgsConstructor;
import org.springframework.util.ObjectUtils;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@AllArgsConstructor
public class PaytmTradeService implements BrokerService<PaytmOrderDTO, PaytmOrderRequestDTO> {

    private final BrokerTokenService<PaytmTokenDTO, String> paytmTokenService;
    private final ReactiveWebClient<PaytmOrderDTO, PaytmOrderRequestDTO> reactiveWebClient;
    private final String uri = "orders/v1/place/regular";

    @Override
    public Mono<PaytmOrderDTO> submitOrder(PaytmOrderRequestDTO paytmOrderRequestDTO) throws BaseTraderException {
            BrokerTokenDTO paytmTokenDTO = paytmTokenService.getToken("").block();
            if (ObjectUtils.isEmpty(paytmTokenDTO)){
                throw new BaseTraderException(ErrorCode.RemoteIOError, "Error retrieving token from Paytm", null);
            }

            Map<String, String> headers = new HashMap<>();
            headers.put("Host","developer.paytmmoney.com");
            headers.put("x-jwt-token", paytmTokenDTO.getAccessToken());

            return reactiveWebClient
                    .call(uri, headers, paytmOrderRequestDTO)
                    .map(dto -> new PaytmOrderDTO(paytmOrderRequestDTO.getOrderId(), dto.getStatus()));
    }
}
