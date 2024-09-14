package com.sb.projects.trader.entity;

import com.sb.projects.trader.enums.InstrumentType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Entity
@ToString
@Table(name = "securities")
public class Security implements JpaEntityObject {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;
    private String securityId;
    private String symbol;
    private String name;
    private String series;
    private String tickSize;
    private String lotSize;
    private InstrumentType instrumentType;
    private String segment;
    private String exchange;
    private String upperLimit;
    private String lowerLimit;
    private String expiryDate;
    private String strikePrice;
    private String freezeQuantity;

}