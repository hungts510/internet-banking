package com.hungts.internetbanking.model.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class TransactionSignature {
    private Integer id;

    private Integer transactionId;

    private String signature;

    private Date createdAt;

    private Date updatedAt;
}
