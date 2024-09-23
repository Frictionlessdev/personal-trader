package com.sb.projects.trader.service;

import com.sb.projects.trader.DTO.OrderDTO;
import com.sb.projects.trader.entity.Order;
import com.sb.projects.trader.enums.OrderStatus;
import com.sb.projects.trader.repository.OrderRepository;
import com.sb.projects.trader.transformer.OrderTransformer;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class OrderServiceImpl implements OrderService {

    private OrderRepository orderRepository;
    private OrderTransformer orderTransformer;

    @Override
    public OrderDTO save(OrderDTO orderDTO) {
        Order order = orderTransformer.transform(orderDTO, (OrderDTO dto) -> {
            Order entity = new Order();
            entity.setId(dto.getId());
            entity.setExchange(dto.getExchange());
            entity.setQuantity(dto.getQuantity());
            entity.setPrice(dto.getPrice());
            entity.setUserId(dto.getUserId());
            entity.setSecurityId((dto.getSecurityId()));
            entity.setStatus(OrderStatus.Saved);
            return entity;
        });

       Order savedOrder = orderRepository.save(order);

       return orderTransformer.transform(savedOrder, (entity) ->
             OrderDTO.builder()
                    .id(entity.getId())
                    .quantity(entity.getQuantity())
                    .exchange(entity.getExchange())
                    .price(entity.getPrice())
                    .securityId(entity.getSecurityId())
                    .userId(entity.getUserId())
                    .status(entity.getStatus())
                    .build()
        );
    }

    @Override
    public List<Order> getPendingOrders() {
        return orderRepository.findByStatus(OrderStatus.Saved);
    }

    @Override
    public Order get(String orderId) {
        return null;
    }

    @Override
    public void updateStatus(String id, OrderStatus status) {
        orderRepository.updateStatus(id, status);
    }

    @Override
    public OrderDTO submit(String orderId) {
        return null;
    }
}
