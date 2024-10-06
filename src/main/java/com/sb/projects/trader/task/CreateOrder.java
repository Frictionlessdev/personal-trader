package com.sb.projects.trader.task;

import com.sb.projects.trader.service.OrderService;
import com.sb.projects.trader.service.StrategyService;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class CreateOrder implements Runnable{

    private final OrderService orderService;
    private final StrategyService strategyService;

    @Override
    public void run() {

    }
}
