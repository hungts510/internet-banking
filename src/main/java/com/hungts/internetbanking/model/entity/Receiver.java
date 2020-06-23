package com.hungts.internetbanking.model.entity;

import lombok.Data;

import java.util.Date;

@Data
public class Receiver {
    private Integer id;

    private Integer userId;

    private String receiverBank;

    private Long receiverAccountNumber;

    private String receiverName;

    private Date createdAt;

    private Date updatedAt;
}