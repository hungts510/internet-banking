package com.hungts.internetbanking.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class DebtorRequest {
    @JsonProperty("user_id")
    private Integer userId;

    @JsonProperty("debtor_account_number")
    private Long debtorAccountNumber;

    @JsonProperty("amount")
    private Long amount;

    @JsonProperty("description")
    private String description;
}
