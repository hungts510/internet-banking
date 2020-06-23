package com.hungts.internetbanking.model.info;

import lombok.Data;

import java.util.Date;

@Data
public class ReceiverInfo {
    private Integer id;

    private Integer userId;

    private String receiverBank;

    private Long receiverAccountNumber;

    private String receiverName;

    private Date createdAt;

    private Date updatedAt;
}
