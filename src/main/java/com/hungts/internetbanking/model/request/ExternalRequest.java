package com.hungts.internetbanking.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ExternalRequest {
    @JsonProperty(value = "message")
    private String message;

    @JsonProperty(value = "account_number")
    private Long accountNumber;

    @JsonProperty(value = "amount")
    private Long amount;

    @JsonProperty(value = "request_time")
    private Long requestTime;
}
