package com.hungts.internetbanking.model.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class Debtor {
    private Integer id;

    private Integer userId;

    private Long debtorAccountNumber;

    private Long amount;

    private String description;

    private Date createdAt;

    private Date updatedAt;

    private Integer status;

    private String receiverName;
}
