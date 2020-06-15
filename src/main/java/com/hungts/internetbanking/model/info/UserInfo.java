package com.hungts.internetbanking.model.info;

import lombok.Data;

@Data
public class UserInfo {
    private Integer userId;

    private String fullName;

    private String email;

    private String phone;
}
