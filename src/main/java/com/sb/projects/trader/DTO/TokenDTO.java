package com.sb.projects.trader.DTO;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TokenDTO implements DataTransferObject {
    private String apiKey;
    private String apiSecret;
    private String requestToken;
}
