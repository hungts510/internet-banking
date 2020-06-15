package com.hungts.internetbanking.mapper;

import com.hungts.internetbanking.model.entity.User;
import com.hungts.internetbanking.model.info.UserInfo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mappings({
            @Mapping(target = "userId", source = "id")
    })
    UserInfo userToUserInfo(User user);
}
