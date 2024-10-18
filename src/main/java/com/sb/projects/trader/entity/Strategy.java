package com.sb.projects.trader.entity;

import com.sb.projects.trader.enums.StrategyStatus;
import com.sb.projects.trader.enums.StrategyType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalTime;

@Getter
@Setter
@Entity
@Table(name = "strategy")
@DynamicUpdate
public class Strategy extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    private String name;
    private StrategyType strategyType;
    private String securityId;
    private double totalMonthlyInvestment;
    private double allocationPercentage;
    private double aggregateInvestment;
    private double actualAggregateInvestment;
    private int tradingDaysInMonth;
    private String userId;
    private StrategyStatus status;
    private double expectedPercentChange;
    private LocalTime startTime;
    private LocalTime endTime;
    private int retryCount;
}
