package com.sb.projects.trader.entity;

import com.sb.projects.trader.enums.Exchange;
import com.sb.projects.trader.enums.InstrumentType;
import com.sb.projects.trader.enums.StrategyOrderStatus;
import com.sb.projects.trader.enums.StrategyType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.DynamicUpdate;

import java.math.BigDecimal;

@ToString
@Getter
@Setter
@Entity
@Table(name = "strategy_order")
@DynamicUpdate
public class StrategyOrder extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private long id;

    private long strategyId;
    private String securityId;
    private StrategyType strategyType;
    private double investment;
    private double orderPrice;
    private double currentMktPrice;
    private double percentChange;
    private Exchange exchange;
    private InstrumentType instrumentType;
    private String userId;
    private StrategyOrderStatus status;
}
