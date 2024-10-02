package com.sb.projects.trader.repository;

import com.sb.projects.trader.entity.Order;
import com.sb.projects.trader.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends CrudRepository<Order, String> {
    List<Order> findByUserId(String userId);
    List<Order> findByStatus(OrderStatus status);

    @Modifying
    @Query("update Order o set o.status = :status where o.id = :id")
    void updateStatus(@Param(value= "id")String id, @Param(value = "status") String status);
}
