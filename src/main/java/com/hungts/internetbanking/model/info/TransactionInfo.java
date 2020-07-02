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
public class TransactionInfo {
    @JsonProperty("transaction_id")
    private Integer id;

    @JsonProperty("from_user_id")
    private Integer fromUserId;

    @JsonProperty("to_user_id")
    private Integer toUserId;

    @JsonProperty("from_account_number")
    private Long fromAccountNumber;

    @JsonProperty("to_account_number")
    private Long toAccountNumber;

    @JsonProperty("amount")
    private Long amount;

    @JsonProperty("type")
    private Integer type;

    @JsonProperty("description")
    private String description;

    @JsonProperty("created_at")
    @JsonSerialize(using = CustomDateSerializer.class)
    @JsonDeserialize(using = CustomDateDeSerializer.class)
    private Date createdAt;

    @JsonProperty("updated_at")
    @JsonSerialize(using = CustomDateSerializer.class)
    @JsonDeserialize(using = CustomDateDeSerializer.class)
    private Date updatedAt;

    @JsonProperty("fromBank")
    private String fromBank;

    @JsonProperty("toBank")
    private String toBank;
}
