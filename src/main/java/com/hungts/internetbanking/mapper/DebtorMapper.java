package com.hungts.internetbanking.mapper;

import com.hungts.internetbanking.model.entity.Debtor;
import com.hungts.internetbanking.model.info.DebtorInfo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring")
public interface DebtorMapper {

    @Mappings({
            @Mapping(target = "id", source = "id"),
            @Mapping(target = "userId", source = "userId"),
            @Mapping(target = "debtorAccountNumber", source = "debtorAccountNumber"),
            @Mapping(target = "amount", source = "amount"),
            @Mapping(target = "description", source = "description"),
            @Mapping(target = "createdAt", source = "createdAt"),
            @Mapping(target = "updatedAt", source = "updatedAt"),
            @Mapping(target = "status", source = "status")
    })
    DebtorInfo debtorToDebtorInfo(Debtor debtor);
}
