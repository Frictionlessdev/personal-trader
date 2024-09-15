package com.sb.projects.trader.DTO.paytm;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.sb.projects.trader.DTO.DataTransferObject;
import lombok.*;

@Getter
@Setter
@JsonSerialize
@JsonDeserialize
public class PaytmOrderDTO implements DataTransferObject {
    @JsonAlias(value = "id")
    private String id;

    public PaytmOrderDTO(String id){
        this.id = id;
    }
}
