package com.hungts.internetbanking.model.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class Transaction {
    private Integer id;

    private Integer fromUserId;

    private Integer toUserId;

    private Long fromAccountNumber;

    private Long toAccountNumber;

    private Long amount;

    private Integer type;

    private Integer status;

    private String description;

    private Date createdAt;

    private Date updatedAt;

    private String fromBank;

    private String toBank;

    private String otp;

    private Integer debtId;
}
