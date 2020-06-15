package com.hungts.internetbanking.model.request;

import lombok.Data;

@Data
public class UserRequest {
    private String fullname;

    private String password;

    private String email;

    private String phone;
}
