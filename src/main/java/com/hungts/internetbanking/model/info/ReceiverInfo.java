package com.hungts.internetbanking.model.info;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.hungts.internetbanking.util.CustomDateDeSerializer;
import com.hungts.internetbanking.util.CustomDateSerializer;
import lombok.Data;

import java.util.Date;

@Data
public class ReceiverInfo {
    @JsonProperty("receiver_id")
    private Integer id;

    @JsonProperty("user_id")
    private Integer userId;

    @JsonProperty("receiver_bank")
    private String receiverBank;

    @JsonProperty("receiver_account_number")
    private Long receiverAccountNumber;

    @JsonProperty("receiver_name")
    private String receiverName;

    @JsonProperty("created_at")
    @JsonSerialize(using = CustomDateSerializer.class)
    @JsonDeserialize(using = CustomDateDeSerializer.class)
    private Date createdAt;

    @JsonProperty("updated_at")
    @JsonSerialize(using = CustomDateSerializer.class)
    @JsonDeserialize(using = CustomDateDeSerializer.class)
    private Date updatedAt;
}
