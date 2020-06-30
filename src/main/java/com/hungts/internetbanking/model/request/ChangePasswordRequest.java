package com.hungts.internetbanking.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ChangePasswordRequest {
    @JsonProperty(value = "email", access = JsonProperty.Access.WRITE_ONLY)
    private String email;

    @JsonProperty(value = "old_password", access = JsonProperty.Access.WRITE_ONLY)
    private String oldPassword;

    @JsonProperty(value = "new_password", access = JsonProperty.Access.WRITE_ONLY)
    private String newPassword;

    @JsonProperty(value = "otp", access = JsonProperty.Access.WRITE_ONLY)
    private String otp;
}
