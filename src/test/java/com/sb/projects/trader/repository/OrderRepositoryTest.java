package com.sb.projects.trader.repository;

import com.sb.projects.trader.entity.Order;
import com.sb.projects.trader.enums.Exchange;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest(properties = {
        "spring.datasource.url=jdbc:h2:mem:dcbapp",
        "spring.jps.hibernate.ddl-auto=create-drop"
}, showSql = true)
class OrderRepositoryTest {

    @Autowired
    OrderRepository orderRepository;

    private Order order;

    @BeforeEach
    void setUp(){
        Order testOrder = new Order();
        testOrder.setSecurityId("198024");
        testOrder.setQuantity(10);
        testOrder.setPrice(100.50D);
        testOrder.setExchange(Exchange.NSE);
        testOrder.setUserId("1xd23rfgt3");

        orderRepository.save(testOrder);
    }

    @Test
    void givenOrder_whenSaved_thenCanBeFoundById(){
        order = orderRepository.findByUserId("1xd23rfgt3").get(0);
    }

    @AfterEach
    void tearDown(){
        orderRepository.delete(order);
    }
}