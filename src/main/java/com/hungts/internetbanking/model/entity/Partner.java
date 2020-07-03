package com.hungts.internetbanking.model.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class Partner {
    private Integer id;

    private String partnerName;

    private String partnerCode;

    private Date createdAt;

    private Date updatedAt;
}
