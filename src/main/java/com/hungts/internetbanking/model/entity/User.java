package com.hungts.internetbanking.model.entity;

import lombok.Data;

@Data
public class User {
    private Integer id;

    private String fullName;

    private String email;

    private String phone;
}
