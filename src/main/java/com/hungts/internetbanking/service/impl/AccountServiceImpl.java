package com.hungts.internetbanking.service.impl;

import com.hungts.internetbanking.define.Constant;
import com.hungts.internetbanking.mapper.AccountMapper;
import com.hungts.internetbanking.model.entity.Account;
import com.hungts.internetbanking.model.info.AccountInfo;
import com.hungts.internetbanking.repository.AccountRepository;
import com.hungts.internetbanking.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class AccountServiceImpl implements AccountService {
    @Autowired
    AccountRepository accountRepository;

    @Override
    public AccountInfo createAccount(AccountInfo accountRequest) {
        Date currentDate = new Date();

        Account accountInsert = Account.builder()
                .accountNumber(generateUniqueAccountNumber())
                .accountType(accountRequest.getAccountType())
                .userId(accountRequest.getUserId())
                .createdAt(currentDate)
                .updatedAt(currentDate)
                .build();

        accountRepository.saveAccount(accountInsert);
        Account account = accountRepository.getAccountById(accountInsert.getId());
        return new AccountInfo(account);
    }

    private long generateUniqueAccountNumber() {
        long currentTimeMilliseconds = System.currentTimeMillis();
        String accountNumberSuffix = String.valueOf(currentTimeMilliseconds % 1000000);
        String accountNumberString = Constant.ACCOUNT_NUMBER_PREFIX + accountNumberSuffix;
        long accountNumber = Long.parseLong(accountNumberString);

        return accountNumber;
    }
}
