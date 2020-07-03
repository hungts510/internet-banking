package com.hungts.internetbanking.mapper;

import com.hungts.internetbanking.model.entity.Receiver;
import com.hungts.internetbanking.model.info.ReceiverInfo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring")
public interface ReceiverMapper {
    @Mappings({
            @Mapping(target = "id", source = "id"),
            @Mapping(target = "userId", source = "userId"),
            @Mapping(target = "receiverBank", source = "receiverBank"),
            @Mapping(target = "receiverAccountNumber", source = "receiverAccountNumber"),
            @Mapping(target = "receiverName", source = "receiverName"),
            @Mapping(target = "createdAt", source = "createdAt"),
            @Mapping(target = "updatedAt", source = "updatedAt")
    })
    ReceiverInfo receiverToReceiverInfo(Receiver receiver);
}
