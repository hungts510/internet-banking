package com.hungts.internetbanking.model.info;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class PartnerInfo {
    private Integer partnerId;

    private String partnerName;

    private String partnerCode;

    private Date createdAt;

    private Date updatedAt;
}
