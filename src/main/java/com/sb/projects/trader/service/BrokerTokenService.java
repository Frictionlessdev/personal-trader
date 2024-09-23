package com.sb.projects.trader.service;

import com.sb.projects.trader.DTO.BrokerTokenDTO;
import com.sb.projects.trader.DTO.DataTransferObject;
import com.sb.projects.trader.DTO.TokenDTO;
import reactor.core.publisher.Mono;

public interface BrokerTokenService<T extends DataTransferObject, U> {
    Mono<T> getToken(U body);
}
