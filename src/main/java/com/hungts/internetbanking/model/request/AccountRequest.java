package com.hungts.internetbanking.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class AccountRequest {

    private Integer userId;

    @JsonProperty(value = "account_bank")
    private String accountBank;

    @JsonProperty(value = "account_number")
    private Long accountNumber;

    @JsonProperty(value = "account_name")
    private String accountName;
}
