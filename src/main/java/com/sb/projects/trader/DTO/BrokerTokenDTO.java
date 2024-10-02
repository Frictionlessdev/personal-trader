package com.sb.projects.trader.DTO;

import lombok.*;

@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BrokerTokenDTO implements DataTransferObject {
    private String accessToken;
}
