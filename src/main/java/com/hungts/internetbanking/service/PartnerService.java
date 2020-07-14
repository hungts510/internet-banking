package com.hungts.internetbanking.service;

import com.hungts.internetbanking.model.info.PartnerInfo;

public interface PartnerService {
    PartnerInfo getPartnerByPartnerCode(String partnerCode);

    PartnerInfo getPartnerByPartnerName(String bankName);
}
