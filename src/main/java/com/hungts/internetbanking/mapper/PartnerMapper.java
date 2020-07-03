package com.hungts.internetbanking.mapper;

import com.hungts.internetbanking.model.entity.Partner;
import com.hungts.internetbanking.model.info.PartnerInfo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring")
public interface PartnerMapper {
    @Mappings({
            @Mapping(target = "partnerId", source = "id"),
            @Mapping(target = "partnerName", source = "partnerName"),
            @Mapping(target = "partnerCode", source = "partnerCode"),
            @Mapping(target = "createdAt", source = "createdAt"),
            @Mapping(target = "updatedAt", source = "updatedAt")
    })
    PartnerInfo partnerToPartnerInfo(Partner partner);
}
