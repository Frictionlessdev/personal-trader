package com.sb.projects.trader.DTO.paytm;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sb.projects.trader.DTO.BrokerErrorDTO;
import lombok.Getter;

import java.util.List;

@Getter
public class PaytmErrorDTO extends BrokerErrorDTO {
    //TODO: Use this error response from paytm
    @JsonProperty(value = "Data")
    private final List<ErrorData> data;

    public PaytmErrorDTO(String status, String message, String errorCode, List<ErrorData> data) {
        super(status, message, errorCode);
        this.data = data;
    }
}
