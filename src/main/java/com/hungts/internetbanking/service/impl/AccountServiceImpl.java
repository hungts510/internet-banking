package com.hungts.internetbanking.service.impl;

import com.hungts.internetbanking.define.Constant;
import com.hungts.internetbanking.exception.EzException;
import com.hungts.internetbanking.mapper.AccountMapper;
import com.hungts.internetbanking.model.entity.Account;
import com.hungts.internetbanking.model.info.AccountInfo;
import com.hungts.internetbanking.repository.AccountRepository;
import com.hungts.internetbanking.service.AccountService;
import org.apache.kafka.common.errors.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class AccountServiceImpl implements AccountService {
    @Autowired
    AccountRepository accountRepository;

    @Override
    public AccountInfo createAccount(AccountInfo accountRequest) {
        Date currentDate = new Date();

        Account accountInsert = new Account();
        accountInsert.setAccountNumber(generateUniqueAccountNumber());
        accountInsert.setAccountType(accountRequest.getAccountType());
        accountInsert.setUserId(accountRequest.getUserId());
        accountInsert.setCreatedAt(currentDate);
        accountInsert.setUpdatedAt(currentDate);

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

    @Override
    public List<AccountInfo> getAllAccountByUserId(Integer userId) {
        List<Account> accountList = accountRepository.getAllAccountByUserId(userId);

        List<AccountInfo> accountInfoList = new ArrayList<>();
        for (Account account : accountList) {
            accountInfoList.add(new AccountInfo(account));
        }
        return accountInfoList;
    }

    @Override
    public AccountInfo getAccountInfoByAccountNumber(long accountNumber) {
        Account account = accountRepository.getCustomerAccountByAccountNumber(accountNumber);

        if (account == null) {
            throw new EzException("Account does not exist!");
        }

        return new AccountInfo(account);
    }
}
