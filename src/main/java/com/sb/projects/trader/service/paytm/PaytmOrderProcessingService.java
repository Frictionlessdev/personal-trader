package com.sb.projects.trader.service.paytm;

import com.sb.projects.trader.service.OrderProcessingService;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;

@Slf4j
@AllArgsConstructor
public class PaytmOrderProcessingService implements OrderProcessingService {

    private final long processorInterval;
    private final long processorInitialDelay;
    private final Runnable submitOrderTask;
    private final ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);

    @Override
    @PostConstruct
    public void processOrder() {
        log.info("Initializing Paytm order processing [PaytmOrderProcessingService]");
        scheduledExecutorService.scheduleAtFixedRate(submitOrderTask,
                processorInitialDelay,
                processorInterval,
                TimeUnit.MILLISECONDS);
    }
}
