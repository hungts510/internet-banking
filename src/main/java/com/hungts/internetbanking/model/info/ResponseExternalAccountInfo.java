package com.hungts.internetbanking.model.info;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ResponseExternalAccountInfo {
    @JsonProperty("account_number")
    private Long accountNumber;

    @JsonProperty("account_type")
    private String accountType;

    @JsonProperty("account_name")
    private String accountName;
}
