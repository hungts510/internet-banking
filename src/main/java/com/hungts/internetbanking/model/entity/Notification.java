package com.hungts.internetbanking.model.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class Notification {
    private Integer id;

    private Integer fromUserId;

    private Integer toUserId;

    private String content;

    private Integer status;

    private Integer debtorId;

    private Date createdAt;

    private Date updatedAt;
}
