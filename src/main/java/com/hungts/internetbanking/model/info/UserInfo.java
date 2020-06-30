package com.hungts.internetbanking.model.info;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(value = "password")
public class UserInfo {
    @JsonProperty("user_id")
    private Integer userId;

    private String password;

    @JsonProperty("full_name")
    private String fullName;

    @JsonProperty("email")
    private String email;

    @JsonProperty("phone")
    private String phone;

    @JsonProperty(value = "role")
    private Integer role;
}
