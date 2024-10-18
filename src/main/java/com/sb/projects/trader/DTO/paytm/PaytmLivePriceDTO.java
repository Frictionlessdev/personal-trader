package com.sb.projects.trader.DTO.paytm;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sb.projects.trader.DTO.DataTransferObject;
import lombok.*;

import java.util.List;

@ToString
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaytmLivePriceDTO implements DataTransferObject {

    @JsonAlias(value = "data")
    private List<PaytmLivePriceDataDTO> data;

}
