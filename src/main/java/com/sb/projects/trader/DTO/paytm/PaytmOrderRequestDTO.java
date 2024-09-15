package com.sb.projects.trader.DTO.paytm;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sb.projects.trader.DTO.DataTransferObject;
import com.sb.projects.trader.enums.Exchange;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Data
@Builder
public class PaytmOrderRequestDTO implements DataTransferObject {
    @JsonProperty("txn_type")
    private String transactionType;

    @JsonProperty("exchange")
    private Exchange exchange;

    @JsonProperty("segment")
    private String segment;

    @JsonProperty("product")
    private String product;

    @JsonProperty("security_id")
    private String securityId;

    @JsonProperty("quantity")
    private int quantity;

    @JsonProperty("validity")
    private String validity;

    @JsonProperty("order_type")
    private String orderType;

    @JsonProperty("price")
    private double price;

    @JsonProperty("source")
    private String source;

    @JsonProperty("off_mkt_flag")
    private String offMktFlag;
}
