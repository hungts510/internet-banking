package com.hungts.internetbanking.service;

import com.hungts.internetbanking.model.info.AccountInfo;
import com.hungts.internetbanking.model.request.TransactionRequest;

import java.util.List;

public interface AccountService {
    AccountInfo createAccount(AccountInfo accountRequest);

    List<AccountInfo> getAllAccountByUserId(Integer userId);

    AccountInfo getAccountInfoByAccountNumber(long accountNumber);

    void payInToAccount(TransactionRequest transactionRequest);

    void transferMoney(TransactionRequest transactionRequest);
}
