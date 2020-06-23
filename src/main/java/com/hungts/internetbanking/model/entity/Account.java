package com.hungts.internetbanking.model.entity;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class Account {
    private Integer id;

    private Long accountNumber;

    private Integer accountType;

    private Long balance;

    private Integer userId;

    private Date createdAt;

    private Date updatedAt;
}
