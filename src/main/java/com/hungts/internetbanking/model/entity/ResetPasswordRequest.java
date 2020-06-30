package com.hungts.internetbanking.model.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class ResetPasswordRequest {
    private int id;

    private int userId;

    private String otp;

    private Date createdAt;

    private int status;
}
