package com.sb.projects.trader.entity;

import com.sb.projects.trader.enums.Exchange;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "orders")
public class Order implements JpaEntityObject{
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
}