package com.hungts.internetbanking.model.info;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hungts.internetbanking.model.entity.Account;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountInfo {
    @JsonProperty("account_id")
    private Integer id;

    @JsonProperty("account_number")
    private Long accountNumber;

    @JsonProperty("account_type")
    private Integer accountType;

    private Long balance;

    @JsonProperty("user_id")
    private Integer userId;

    @JsonProperty("created_at")
    private Date createdAt;

    @JsonProperty("updated_at")
    private Date updatedAt;

    @JsonProperty("account_name")
    private String accountName;

    public AccountInfo(Account account) {
        this.id = account.getId();
        this.accountNumber = account.getAccountNumber();
        this.accountType = account.getAccountType();
        this.balance = account.getBalance();
        this.userId = account.getUserId();
        this.createdAt = account.getCreatedAt();
        this.updatedAt = account.getUpdatedAt();
    }
}
