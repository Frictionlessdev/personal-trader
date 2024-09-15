package com.sb.projects.trader.DTO.paytm;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.sb.projects.trader.DTO.BrokerTokenDTO;
import lombok.Getter;

@Getter
public class PaytmTokenDTO extends BrokerTokenDTO {
    @JsonAlias(value = "merchant_id")
    private final String merchantId;
    @JsonAlias(value = "channel_id")
    private final String channelId;
    @JsonAlias(value = "api_key")
    private final String apiKey;
    @JsonAlias(value = "access_token")
    private final String paytmAccessToken;
    @JsonAlias(value = "public_access_token")
    private final String publicAccessToken;
    @JsonAlias(value = "read_access_token")
    private final String readAccessToken;

    public PaytmTokenDTO(String accessToken, String merchantId, String channelId, String apiKey, String paytmAccessToken, String publicAccessToken, String readAccessToken) {
        super(paytmAccessToken);
        this.merchantId = merchantId;
        this.channelId = channelId;
        this.apiKey = apiKey;
        this.paytmAccessToken = paytmAccessToken;
        this.publicAccessToken = publicAccessToken;
        this.readAccessToken = readAccessToken;
    }
}
