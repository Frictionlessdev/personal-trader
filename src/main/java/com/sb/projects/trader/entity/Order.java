package com.sb.projects.trader.entity;

import com.sb.projects.trader.DTO.BrokerErrorDTO;
import com.sb.projects.trader.entity.converter.RawBrokerErrorConverter;
import com.sb.projects.trader.enums.Exchange;
import com.sb.projects.trader.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.DynamicUpdate;

@ToString
@Getter
@Setter
@Entity
@Table(name = "orders")
@DynamicUpdate
public class Order extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    private String id;

    @Column(name = "securityId", nullable = false)
    private String securityId;

    @Column(name = "exchange", nullable = false)
    private Exchange exchange;

    @Column(name = "quantity", nullable = false)
    private int quantity;

    @Column(name = "price", nullable = false)
    private double price;

    @Column(name = "userId", nullable = false)
    private String userId;

    @Column(name = "status", nullable = false)
    private OrderStatus status;

    @Convert(converter = RawBrokerErrorConverter.class)
    @Column(name = "rawBrokerError")
    private BrokerErrorDTO rawBrokerError;
}