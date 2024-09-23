package com.sb.projects.trader.service.paytm;

import com.sb.projects.trader.DTO.paytm.PaytmOrderDTO;
import com.sb.projects.trader.DTO.paytm.PaytmOrderRequestDTO;
import com.sb.projects.trader.DTO.paytm.PaytmTokenDTO;
import com.sb.projects.trader.DTO.paytm.PaytmTokenRequestDTO;
import com.sb.projects.trader.config.ApplicationConfig;
import com.sb.projects.trader.entity.Order;
import com.sb.projects.trader.enums.ErrorCode;
import com.sb.projects.trader.enums.OrderStatus;
import com.sb.projects.trader.exceptions.BaseTraderException;
import com.sb.projects.trader.service.BrokerService;
import com.sb.projects.trader.service.BrokerTokenService;
import com.sb.projects.trader.service.OrderProcessingService;
import com.sb.projects.trader.service.OrderService;
import com.sb.projects.trader.transformer.BaseEntityTransformer;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.*;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
public class PaytmOrderProcessingService implements OrderProcessingService {

    private final long processorInterval;
    private final long processorInitialDelay;
    private final OrderService orderService;
    private final BrokerService<PaytmOrderDTO, PaytmOrderRequestDTO> brokerService;
    private final BaseEntityTransformer<Order, PaytmOrderRequestDTO> orderRequestTransformer;
    private final ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);

    @Override
    @PostConstruct
    public void processOrder() {
        log.info("Initializing Paytm order processing [PaytmOrderProcessingService]");
        scheduledExecutorService.scheduleAtFixedRate(new ScheduledTask(),
                processorInitialDelay,
                processorInterval,
                TimeUnit.MILLISECONDS);
    }

    class ScheduledTask implements Runnable{

        @Override
        public void run() {
           List<Order> pendingOrders = orderService.getPendingOrders();

           List<PaytmOrderDTO> paytmOrders =
           pendingOrders.stream().map(pendingOrder -> orderRequestTransformer.transform(pendingOrder, order ->
                   PaytmOrderRequestDTO.builder()
                           .orderId(order.getId())
                           .offMktFlag("true")
                           .source("N")
                           .price(order.getPrice())
                           .validity("DAY")
                           .quantity(order.getQuantity())
                           .securityId(order.getSecurityId())
                           .product("C")
                           .orderType("MKT")
                           .transactionType("B")
                           .exchange(order.getExchange())
                           .segment("E").build()))
                   .map(this::submit)
                   .filter(Optional::isPresent).map(Optional::get).toList();

           for(PaytmOrderDTO paytmOrder : paytmOrders){
               log.info(paytmOrder.toString());
               log.info(orderService.get(paytmOrder.getId()).toString());
           }

        }

        private Optional<PaytmOrderDTO> submit(PaytmOrderRequestDTO paytmOrderRequestDTO){
            try {
                PaytmOrderDTO paytmOrderDTO = brokerService.submitOrder(paytmOrderRequestDTO).block();
                log.info("Order id {} submitted successfully", paytmOrderRequestDTO.getOrderId());
                orderService.updateStatus(paytmOrderRequestDTO.getOrderId(), OrderStatus.Submitted);
                return Optional.ofNullable(paytmOrderDTO);
            } catch (BaseTraderException e) {
                log.info("Error submitting order id: {} to paytm", paytmOrderRequestDTO.getOrderId());
                log.info(e.getMessage());
                orderService.updateStatus(paytmOrderRequestDTO.getOrderId(), OrderStatus.Rejected);
                return Optional.empty();
            }
        }
    }
}
