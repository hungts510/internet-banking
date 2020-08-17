package com.hungts.internetbanking.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class RefreshTokenRequest {

    @JsonProperty(value = "phone")
    private String phone;

    @JsonProperty(value = "refresh_token")
    private String refreshToken;


}
