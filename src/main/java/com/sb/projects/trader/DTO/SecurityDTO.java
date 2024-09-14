package com.sb.projects.trader.DTO;

import com.sb.projects.trader.enums.InstrumentType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SecurityDTO implements DataTransferObject {
    private String id;
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
