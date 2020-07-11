package com.hungts.internetbanking.model.info;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.hungts.internetbanking.util.CustomDateDeSerializer;
import com.hungts.internetbanking.util.CustomDateSerializer;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class DebtorInfo {
    @JsonProperty(value = "debtor_id")
    private Integer id;

    @JsonProperty(value = "user_id")
    private Integer userId;

    @JsonProperty(value = "debtor_account_number")
    private Long debtorAccountNumber;

    @JsonProperty(value = "amount")
    private Long amount;

    @JsonProperty(value = "description")
    private String description;

    @JsonProperty(value = "created_at")
    @JsonSerialize(using = CustomDateSerializer.class)
    @JsonDeserialize(using = CustomDateDeSerializer.class)
    private Date createdAt;

    @JsonProperty(value = "updated_at")
    @JsonSerialize(using = CustomDateSerializer.class)
    @JsonDeserialize(using = CustomDateDeSerializer.class)
    private Date updatedAt;

    @JsonProperty(value = "status")
    private Integer status;

    @JsonProperty(value = "receiver_name")
    private String receiverName;

    @JsonProperty(value = "reveiver_account_number")
    private Long receiverAccountNumber;
}
