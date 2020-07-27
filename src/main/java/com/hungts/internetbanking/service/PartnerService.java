package com.hungts.internetbanking.service;

import com.hungts.internetbanking.model.info.PartnerInfo;

import java.util.List;

public interface PartnerService {
    PartnerInfo getPartnerByPartnerCode(String partnerCode);

    PartnerInfo getPartnerByPartnerName(String bankName);

    List<PartnerInfo> getListPartners();
}
