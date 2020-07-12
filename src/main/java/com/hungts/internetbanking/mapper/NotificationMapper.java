package com.hungts.internetbanking.mapper;

import com.hungts.internetbanking.model.entity.Notification;
import com.hungts.internetbanking.model.info.NotificationInfo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring")
public interface NotificationMapper {
    @Mappings({
            @Mapping(target = "id", source = "id"),
            @Mapping(target = "fromUserId", source = "fromUserId"),
            @Mapping(target = "toUserId", source = "toUserId"),
            @Mapping(target = "content", source = "content"),
            @Mapping(target = "status", source = "status"),
            @Mapping(target = "debtorId", source = "debtorId"),
            @Mapping(target = "createdAt", source = "createdAt"),
            @Mapping(target = "updatedAt", source = "updatedAt")
    })
    NotificationInfo notificationToNotificationInfo(Notification notification);
}
