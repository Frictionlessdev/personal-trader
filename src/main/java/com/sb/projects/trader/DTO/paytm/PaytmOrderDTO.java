package com.sb.projects.trader.DTO.paytm;

import com.sb.projects.trader.DTO.DataTransferObject;
import lombok.*;

@ToString
@Getter
public class PaytmOrderDTO implements DataTransferObject {
    private final String id;
    private final String status;

    public PaytmOrderDTO(String id, String status) {
        this.status = status;
        this.id = id;
    }
}
