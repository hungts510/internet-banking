package com.hungts.internetbanking.mapper;

import com.hungts.internetbanking.model.entity.Account;
import com.hungts.internetbanking.model.entity.User;
import com.hungts.internetbanking.model.info.AccountInfo;
import com.hungts.internetbanking.model.info.UserInfo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring")
public interface AccountMapper {
    @Mappings({
    })
    AccountInfo accountToAccountInfo(Account account);
}
