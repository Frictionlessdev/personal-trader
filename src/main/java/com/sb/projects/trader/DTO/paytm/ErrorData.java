package com.sb.projects.trader.DTO.paytm;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ErrorData {
    @JsonProperty(value = "oms_error_code")
    private final String omsErrorCode;
}
