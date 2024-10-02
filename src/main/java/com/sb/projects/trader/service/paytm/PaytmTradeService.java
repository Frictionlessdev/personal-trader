package com.sb.projects.trader.service.paytm;

import com.sb.projects.trader.DTO.BrokerTokenDTO;
import com.sb.projects.trader.DTO.paytm.PaytmErrorDTO;
import com.sb.projects.trader.DTO.paytm.PaytmOrderDTO;
import com.sb.projects.trader.DTO.paytm.PaytmOrderRequestDTO;
import com.sb.projects.trader.DTO.paytm.PaytmTokenDTO;
import com.sb.projects.trader.enums.ErrorCode;
import com.sb.projects.trader.exceptions.BaseTraderException;
import com.sb.projects.trader.service.BrokerService;
import com.sb.projects.trader.service.BrokerTokenService;
import com.sb.projects.trader.utils.ReactiveWebClient;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ObjectUtils;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Slf4j
@AllArgsConstructor
public class PaytmTradeService implements BrokerService<PaytmOrderDTO, PaytmOrderRequestDTO> {

    private final BrokerTokenService<PaytmTokenDTO> paytmTokenService;
    private final ReactiveWebClient<PaytmOrderDTO, PaytmOrderRequestDTO, PaytmErrorDTO> reactiveWebClient;
    private final String uri = "orders/v1/place/regular";

    @Override
    public Mono<PaytmOrderDTO> submitOrder(PaytmOrderRequestDTO paytmOrderRequestDTO) throws BaseTraderException {
        Map<String, String> headers = new HashMap<>();

        return paytmTokenService.getToken()
                .doOnError(t -> log.error("Remote token request  responded with error '{}'", t.getMessage()))
                .flatMap((BrokerTokenDTO dto) -> {
                    log.info("Remote token request complete with {}", dto.toString());

                    headers.put("Host", "developer.paytmmoney.com");
                    headers.put("x-jwt-token", dto.getAccessToken());

                    return reactiveWebClient
                            .call(uri, headers, paytmOrderRequestDTO);
                });
    }
}
