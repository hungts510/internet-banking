package com.hungts.internetbanking.service;

import com.hungts.internetbanking.model.info.AccountInfo;

import java.util.List;

public interface AccountService {
    AccountInfo createAccount(AccountInfo accountRequest);

    List<AccountInfo> getAllAccountByUserId(Integer userId);
}
