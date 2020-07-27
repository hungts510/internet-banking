package com.hungts.internetbanking.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.hungts.internetbanking.util.CustomDateDeSerializer;
import com.hungts.internetbanking.util.CustomDateSerializer;
import lombok.Data;

import java.util.Date;

@Data
public class AdminViewRequest {

    @JsonProperty("date_from")
    @JsonSerialize(using = CustomDateSerializer.class)
    @JsonDeserialize(using = CustomDateDeSerializer.class)
    private Date fromDate;

    @JsonProperty("date_to")
    @JsonSerialize(using = CustomDateSerializer.class)
    @JsonDeserialize(using = CustomDateDeSerializer.class)
    private Date toDate;

    @JsonProperty("bank_name")
    private String bankName;
}
