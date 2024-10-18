package com.sb.projects.trader.service;

import com.sb.projects.trader.DTO.DataTransferObject;
import com.sb.projects.trader.enums.Exchange;
import com.sb.projects.trader.enums.InstrumentType;
import com.sb.projects.trader.exceptions.BaseTraderException;
import reactor.core.publisher.Mono;

public interface BrokerPriceService<T extends DataTransferObject> {
    Mono<T> getLivePrice(String securityId, InstrumentType instrumentType, Exchange exchange) throws BaseTraderException;
}
