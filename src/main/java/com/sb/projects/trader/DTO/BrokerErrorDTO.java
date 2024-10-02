package com.sb.projects.trader.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Builder
@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BrokerErrorDTO implements DataTransferObject {
    @JsonProperty(value = "status")
    private String status;

    @JsonProperty(value = "message")
    private String message;

    @JsonProperty(value = "error_code")
    private String error_code;
}
