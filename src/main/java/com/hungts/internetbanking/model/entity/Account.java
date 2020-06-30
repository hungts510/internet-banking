package com.hungts.internetbanking.model.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class Account {
    private Integer id;

    private Long accountNumber;

    private Integer accountType;

    private Long balance;

    private Integer userId;

    private Date createdAt;

    private Date updatedAt;

    public Account(Integer id, Long accountNumber, Integer accountType, Integer userId, Date createdAt, Date updatedAt) {
        this.id = id;
        this.accountNumber = accountNumber;
        this.accountType = accountType;
        this.userId = userId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
