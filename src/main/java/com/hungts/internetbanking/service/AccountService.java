package com.hungts.internetbanking.service;

import com.hungts.internetbanking.model.info.AccountInfo;

public interface AccountService {
    AccountInfo createAccount(AccountInfo accountRequest);
}
