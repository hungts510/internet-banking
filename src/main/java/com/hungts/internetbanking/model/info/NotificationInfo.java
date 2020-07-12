package com.hungts.internetbanking.model.info;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.hungts.internetbanking.util.CustomDateDeSerializer;
import com.hungts.internetbanking.util.CustomDateSerializer;
import lombok.Data;

import java.util.Date;

@Data
public class NotificationInfo {
    @JsonProperty(value = "id")
    private Integer id;

    @JsonProperty(value = "from_user_id")
    private Integer fromUserId;

    @JsonProperty(value = "to_user_id")
    private Integer toUserId;

    @JsonProperty(value = "content")
    private String content;

    @JsonProperty(value = "status")
    private Integer status;

    @JsonProperty(value = "debtor_id")
    private Integer debtorId;

    @JsonProperty(value = "created_at")
    @JsonSerialize(using = CustomDateSerializer.class)
    @JsonDeserialize(using = CustomDateDeSerializer.class)
    private Date createdAt;

    @JsonProperty(value = "updated_at")
    @JsonSerialize(using = CustomDateSerializer.class)
    @JsonDeserialize(using = CustomDateDeSerializer.class)
    private Date updatedAt;
}
