package com.hungts.internetbanking.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TransactionRequest {
    @JsonProperty("transaction_id")
    private Integer transactionId;

    @JsonProperty("from_user_id")
    private Integer fromUserId;

    @JsonProperty("to_user_id")
    private Integer toUserId;

    @JsonProperty("from_account_number")
    private Long fromAccountNumber;

    @JsonProperty("to_account_number")
    private Long toAccountNumber;

    @JsonProperty("amount")
    private Long amount;

    @JsonProperty("type")
    private Integer type;

    @JsonProperty("description")
    private String description;

    @JsonProperty("from_bank")
    private String fromBank;

    @JsonProperty("to_bank")
    private String toBank;

    @JsonProperty("otp")
    private String otp;
}
