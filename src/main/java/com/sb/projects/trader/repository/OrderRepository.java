package com.sb.projects.trader.repository;

import com.sb.projects.trader.entity.Order;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface OrderRepository extends CrudRepository<Order, Long> {
    List<Order> findByUserId(String userId);
}
