package com.hungts.internetbanking.mapper;

import com.hungts.internetbanking.model.entity.Transaction;
import com.hungts.internetbanking.model.info.TransactionInfo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring")
public interface TransactionMapper {

    @Mappings({
            @Mapping(target = "id", source = "id"),
            @Mapping(target = "fromUserId", source = "fromUserId"),
            @Mapping(target = "toUserId", source = "toUserId"),
            @Mapping(target = "fromAccountNumber", source = "fromAccountNumber"),
            @Mapping(target = "toAccountNumber", source = "toAccountNumber"),
            @Mapping(target = "amount", source = "amount"),
            @Mapping(target = "type", source = "type"),
            @Mapping(target = "description", source = "description"),
            @Mapping(target = "createdAt", source = "createdAt"),
            @Mapping(target = "updatedAt", source = "updatedAt"),
            @Mapping(target = "fromBank", source = "fromBank"),
            @Mapping(target = "toBank", source = "toBank"),
            @Mapping(target = "userPayFee", source = "payFee"),
    })
    TransactionInfo transactionToTransactionInfo(Transaction transaction);
}
