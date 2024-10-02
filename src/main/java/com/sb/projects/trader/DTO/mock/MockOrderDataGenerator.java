package com.sb.projects.trader.DTO.mock;

import com.sb.projects.trader.entity.Order;
import com.sb.projects.trader.enums.Exchange;
import com.sb.projects.trader.enums.OrderStatus;
import com.sb.projects.trader.repository.OrderRepository;
import com.sb.projects.trader.service.OrderService;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.weaver.ast.Or;

@Slf4j
@AllArgsConstructor
public class MockOrderDataGenerator {

    private final OrderRepository orderRepository;

    @PostConstruct
    public void generate(){
        log.info("Generating Mock orders in order Repository [MockOrderDataGenerator]");
        /*Order order = new Order();
        order.setSecurityId("11223344");
        order.setPrice(1234.9);
        order.setUserId("test@test.com");
        order.setQuantity(199);
        order.setExchange(Exchange.NSE);
        order.setStatus(OrderStatus.Saved);

        orderRepository.save(order);*/

        Order order = new Order();
        order.setSecurityId("19084");
        order.setPrice(StrictMath.absExact(0));
        order.setUserId("test@test.com");
        order.setQuantity(1);
        order.setExchange(Exchange.NSE);
        order.setStatus(OrderStatus.Saved);

        orderRepository.save(order);
    }
}
