package com.hungts.internetbanking.mapper;

import com.hungts.internetbanking.model.entity.Receiver;
import com.hungts.internetbanking.model.info.ReceiverInfo;
import org.mapstruct.Mapper;
import org.mapstruct.Mappings;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring")
public interface ReceiverMapper {
    @Mappings({
    })
    ReceiverInfo receiverToReceiverInfo(Receiver receiver);
}
