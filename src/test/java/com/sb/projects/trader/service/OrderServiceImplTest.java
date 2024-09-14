package com.sb.projects.trader.service;

import com.sb.projects.trader.DTO.OrderDTO;
import com.sb.projects.trader.entity.Order;
import com.sb.projects.trader.enums.Exchange;
import com.sb.projects.trader.repository.OrderRepository;
import com.sb.projects.trader.transformer.OrderTransformer;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.hamcrest.MatcherAssert.assertThat;

@ExtendWith(SpringExtension.class)
public class OrderServiceImplTest {

    @Spy
    OrderRepository orderRepository;

    OrderTransformer orderTransformer = new OrderTransformer();

    OrderServiceImpl orderService;

    @Captor
    ArgumentCaptor<Order> actual;

    @BeforeEach
    void setUp(){
        orderService = new OrderServiceImpl(orderRepository, orderTransformer);
    }

    @Test
    void createOrderTest(){
        OrderDTO orderDTO = OrderDTO.builder()
                .id("Test id")
                .price(1000.001)
                .quantity(100)
                .exchange(Exchange.NSE)
                .userId("Test userId")
                .build();

        Order expected = new Order();
        expected.setId("Test id");
        expected.setPrice(1000.001);
        expected.setQuantity(100);
        expected.setExchange(Exchange.NSE);
        expected.setUserId("Test userId");

        Mockito.doReturn(expected).when(orderRepository).save(Mockito.any());

        orderService.save(orderDTO);

        Mockito.verify(orderRepository).save(actual.capture());
        assertThat(expected, Matchers.samePropertyValuesAs(actual.getValue()));
    }

    @Test
    void submitOrderTest(){
        Order order = new Order();
        order.setId("Test id");
        order.setPrice(1000.001);
        order.setQuantity(100);
        order.setExchange(Exchange.NSE);
        order.setUserId("Test userId");

        Mockito.doReturn(order).when(orderRepository).findById(Mockito.any());
    }
}
