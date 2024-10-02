package com.sb.projects.trader.service;

import com.sb.projects.trader.DTO.BrokerErrorDTO;
import com.sb.projects.trader.DTO.OrderDTO;
import com.sb.projects.trader.entity.Order;
import com.sb.projects.trader.enums.ErrorCode;
import com.sb.projects.trader.enums.OrderStatus;
import com.sb.projects.trader.exceptions.BaseTraderException;
import com.sb.projects.trader.repository.OrderRepository;
import com.sb.projects.trader.transformer.OrderTransformer;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;

@Slf4j
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
    public Order get(String orderId) throws BaseTraderException {
        Optional<Order> order = orderRepository.findById(orderId);
        if (order.isPresent()){
            return order.get();
        }

        log.info("Error retrieving Order Id: {}", orderId);
        throw new BaseTraderException(ErrorCode.EntityNotFound,
                String.format("Order with Order Id : %s not found", orderId), null);
    }

    @Override
    public OrderDTO submit(String orderId, OrderStatus status, BrokerErrorDTO brokerErrorDTO) throws BaseTraderException {
        Optional<Order> order = orderRepository.findById(orderId);
        if (order.isPresent()) {
            order.get().setStatus(status);
            order.get().setRawBrokerError(brokerErrorDTO);

            try {
                orderRepository.save(order.get());
            } catch (Exception e){
                log.error("Error updating order id: '{}' to status: '{}'", orderId, status);
                throw new BaseTraderException(ErrorCode.DBError,
                        String.format("Error updating order with Order Id : '%s' to status: '%s'", orderId, status), null);
            }

            return orderTransformer.transform(order.get(), (entity) ->
                    OrderDTO.builder()
                            .id(entity.getId())
                            .quantity(entity.getQuantity())
                            .exchange(entity.getExchange())
                            .price(entity.getPrice())
                            .securityId(entity.getSecurityId())
                            .userId(entity.getUserId())
                            .status(entity.getStatus())
                            .rawBrokerErrorDTO(entity.getRawBrokerError())
                            .build()
            );
        }

        log.info("Error Submitting Order Id: {} and status: {}", orderId, status);
        throw new BaseTraderException(ErrorCode.EntityNotFound,
                String.format("Order with Order Id : %s not found", orderId), null);
    }
}
