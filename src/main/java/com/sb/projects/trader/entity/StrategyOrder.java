package com.sb.projects.trader.entity;

import com.sb.projects.trader.enums.StrategyType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "strategy_order")
public class StrategyOrder implements JpaEntityObject{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    private String securityId;
    private StrategyType strategyType;
    private double aggregateInvestment;
    private double orderPrice;
    private double currentMktPrice;
    private double percentChange;
    private String user;
}
