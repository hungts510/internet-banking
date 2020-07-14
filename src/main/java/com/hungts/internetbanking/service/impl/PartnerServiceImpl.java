package com.hungts.internetbanking.service.impl;

import com.hungts.internetbanking.exception.EzException;
import com.hungts.internetbanking.mapper.PartnerMapper;
import com.hungts.internetbanking.model.entity.Partner;
import com.hungts.internetbanking.model.info.PartnerInfo;
import com.hungts.internetbanking.repository.PartnerRepository;
import com.hungts.internetbanking.service.PartnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PartnerServiceImpl implements PartnerService {

    @Autowired
    PartnerRepository partnerRepository;

    @Autowired
    PartnerMapper partnerMapper;

    @Override
    public PartnerInfo getPartnerByPartnerCode(String partnerCode) {
        Partner partner = partnerRepository.getPartnerByPartnerCode(partnerCode);

        if (partner == null) {
            throw new EzException("Partner does not exist");
        }

        return partnerMapper.partnerToPartnerInfo(partner);
    }

    @Override
    public PartnerInfo getPartnerByPartnerName(String bankName) {
        Partner partner = partnerRepository.getPartnerByPartnerCode(bankName);

        if (partner == null) {
            throw new EzException("Partner does not exist");
        }

        return partnerMapper.partnerToPartnerInfo(partner);
    }
}
