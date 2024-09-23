package com.sb.projects.trader.service;

import com.sb.projects.trader.DTO.DataTransferObject;
import com.sb.projects.trader.entity.Order;
import com.sb.projects.trader.exceptions.BaseTraderException;
import reactor.core.publisher.Mono;

public interface BrokerService<T extends DataTransferObject, U extends DataTransferObject> {
    Mono<T> submitOrder(U order) throws BaseTraderException;
}
