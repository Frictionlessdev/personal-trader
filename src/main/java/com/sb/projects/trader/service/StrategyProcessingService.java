package com.sb.projects.trader.service;

import com.sb.projects.trader.task.CreateStrategyOrder;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@AllArgsConstructor
public class StrategyProcessingService implements OrderProcessingService {

    private final long initialDelay;
    private final long period;
    private Runnable createStrategyOrderTask;
    private Runnable createOrderTask;
    private final ScheduledExecutorService scheduledExecutorService =
            Executors.newSingleThreadScheduledExecutor();

    @Override
    @PostConstruct
    public void processOrder() {
        scheduledExecutorService.scheduleAtFixedRate(createStrategyOrderTask,
                initialDelay, period, TimeUnit.MILLISECONDS);
        scheduledExecutorService.scheduleAtFixedRate(createOrderTask,
                initialDelay, period, TimeUnit.MILLISECONDS);
    }
}
