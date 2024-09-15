package com.sb.projects.trader.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BrokerTokenDTO implements DataTransferObject {
    private String accessToken;
}
