package com.sb.projects.trader.entity;

import com.sb.projects.trader.enums.StrategyType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "strategy")
public class Strategy implements JpaEntityObject {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    private StrategyType strategyType;
    private String securityId;
    private double totalMonthlyInvestment;
    private double allocationPercentage;
    private int tradingDaysInMonth;
    private String user;
}
