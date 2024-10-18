package com.sb.projects.trader.service.paytm;

import com.sb.projects.trader.DTO.BrokerTokenDTO;
import com.sb.projects.trader.DTO.DataTransferObject;
import com.sb.projects.trader.DTO.paytm.*;
import com.sb.projects.trader.enums.Exchange;
import com.sb.projects.trader.enums.InstrumentType;
import com.sb.projects.trader.exceptions.BaseTraderException;
import com.sb.projects.trader.service.BrokerPriceService;
import com.sb.projects.trader.service.BrokerTokenService;
import com.sb.projects.trader.utils.ReactiveWebClient;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@AllArgsConstructor
public class PaytmPriceService implements BrokerPriceService<PaytmLivePriceDTO> {

    private final BrokerTokenService<PaytmTokenDTO> paytmTokenService;
    private final ReactiveWebClient<PaytmLivePriceDTO, DataTransferObject, PaytmErrorDTO> reactiveWebClient;
    private final String uri = "/data/v1/price/live";

    @Override
    public Mono<PaytmLivePriceDTO> getLivePrice(String securityId, InstrumentType instrumentType, Exchange exchange) throws BaseTraderException {
        Map<String, String> headers = new HashMap<>();
        return paytmTokenService.getToken()
                .doOnError(t -> log.error("Remote token request  responded with error '{}'", t.getMessage()))
                .flatMap((BrokerTokenDTO dto) -> {
                    log.info("Remote token request complete with {}", dto.toString());

                    headers.put("Host", "developer.paytmmoney.com");
                    headers.put("x-jwt-token", dto.getAccessToken());

                    Map<String, String> queryParams = Map.of("mode", "LTP", "pref",
                            String.format("%s:%s:%s", exchange, securityId, instrumentType));

                    return reactiveWebClient
                            .get(uri, headers, queryParams);
                });
    }
}
