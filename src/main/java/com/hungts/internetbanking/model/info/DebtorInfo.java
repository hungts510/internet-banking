package com.hungts.internetbanking.model.info;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class DebtorInfo {
    @JsonProperty(value = "debtor_id")
    private Integer id;

    @JsonProperty(value = "user_id")
    private Integer userId;

    @JsonProperty(value = "debtor_account_number")
    private Long debtorAccountNumber;

    @JsonProperty(value = "amount")
    private Long amount;

    @JsonProperty(value = "description")
    private String description;

    @JsonProperty(value = "created_at")
    private Date createdAt;

    @JsonProperty(value = "updated_at")
    private Date updatedAt;

    @JsonProperty(value = "status")
    private Integer status;
}
