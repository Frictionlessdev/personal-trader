package com.sb.projects.trader.DTO.paytm;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.*;

@ToString
@Getter
@Setter
@Builder
@NoArgsConstructor
public class PaytmLivePriceDataDTO {

    @JsonAlias(value = "tradable")
    private String tradable;

    @JsonAlias(value = "mode")
    private String mode;

    @JsonAlias(value = "security_id")
    private int securityId;

    @JsonAlias(value = "last_price")
    private double lastPrice;

    @JsonAlias(value = "change_percent")
    private double changePercent;

    @JsonAlias(value = "change_absolute")
    private double changeAbsolute;

    @JsonAlias(value = "last_trade_time")
    private long lastTradeTime;

    @JsonAlias(value = "found")
    private boolean found;

    public PaytmLivePriceDataDTO(String tradable, String mode, int securityId,
                                  double lastPrice, double changePercent, double changeAbsolute,
                                  long lastTradeTime, boolean found){
        this.tradable = tradable;
        this.mode = mode;
        this.securityId = securityId;
        this.lastPrice = lastPrice;
        this.changePercent = changePercent;
        this.changeAbsolute = changeAbsolute;
        this.lastTradeTime = lastTradeTime;
        this.found = found;
    }

}
