package com.hungts.internetbanking.model.info;

import com.hungts.internetbanking.model.entity.Account;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountInfo {
    private Integer id;

    private Long accountNumber;

    private Integer accountType;

    private Long balance;

    private Integer userId;

    private Date createdAt;

    private Date updatedAt;

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
