package com.sb.projects.trader.task;

import com.sb.projects.trader.DTO.paytm.PaytmOrderDTO;
import com.sb.projects.trader.DTO.paytm.PaytmOrderRequestDTO;
import com.sb.projects.trader.entity.Order;
import com.sb.projects.trader.enums.OrderStatus;
import com.sb.projects.trader.exceptions.BaseTraderException;
import com.sb.projects.trader.exceptions.BrokerHttpException;
import com.sb.projects.trader.service.BrokerService;
import com.sb.projects.trader.service.OrderService;
import com.sb.projects.trader.transformer.BaseEntityTransformer;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@AllArgsConstructor
public class SubmitOrder implements Runnable {
    private final OrderService orderService;
    private final BrokerService<PaytmOrderDTO, PaytmOrderRequestDTO> brokerService;
    private final BaseEntityTransformer<Order, PaytmOrderRequestDTO> orderRequestTransformer;

    @SneakyThrows
    @Override
    public void run() {
        log.info("Running submit trade task...");
        List<Order> pendingOrders = orderService.getPendingOrders();

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
                .forEach(this::submit);

        for (Order order : pendingOrders) {
            try {
                log.info("Checking status: {}", orderService.get(order.getId()).toString());
            } catch (BaseTraderException e) {
                log.info("Error retrieving Order Id: {} after submit from store", order.getId());
            }
        }
    }

    protected void submit(PaytmOrderRequestDTO paytmOrderRequestDTO) {
        try {
            orderService.submit(paytmOrderRequestDTO.getOrderId(), OrderStatus.Processing, null);
            brokerService.submitOrder(paytmOrderRequestDTO).subscribe(
                    dto -> {
                        try {
                            orderService.submit(paytmOrderRequestDTO.getOrderId(), OrderStatus.Submitted, null);
                            log.info("Order id '{}' submitted successfully to Paytm", paytmOrderRequestDTO.getOrderId());
                        } catch (BaseTraderException e) {
                            log.error("Error updating Order Id: '{}' to 'submitted' in store", paytmOrderRequestDTO.getOrderId());
                        }
                    },
                    err -> {
                        var dto = ((BrokerHttpException)err).getBrokerErrorDTO();
                        log.error("Error submitting order id: '{}' to paytm", paytmOrderRequestDTO.getOrderId());
                        log.error("Error from remote broker: {}", dto);
                        try {
                            orderService.submit(paytmOrderRequestDTO.getOrderId(), OrderStatus.Rejected, dto);
                        } catch (BaseTraderException ex) {
                            log.error("Error updating Order id: '{}' to 'rejected' in store", paytmOrderRequestDTO.getOrderId());
                        }
                    });

        }  catch (Exception e){
            log.error("Unhandled exception: {}", String.valueOf(e.getMessage()));
        }
    }
}
