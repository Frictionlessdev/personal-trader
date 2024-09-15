package com.sb.projects.trader.DTO.paytm;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sb.projects.trader.DTO.DataTransferObject;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PaytmTokenRequestDTO implements DataTransferObject {
    @JsonProperty("api_key")
    private String apiKey;

    @JsonProperty("api_secret_key")
    private String apiSecret;

    @JsonProperty("request_token")
    private String requestToken;
}
