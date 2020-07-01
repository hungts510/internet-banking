package com.hungts.internetbanking.service.impl;

import com.hungts.internetbanking.define.Constant;
import com.hungts.internetbanking.exception.EzException;
import com.hungts.internetbanking.model.entity.Account;
import com.hungts.internetbanking.model.entity.Transaction;
import com.hungts.internetbanking.model.info.AccountInfo;
import com.hungts.internetbanking.model.info.UserInfo;
import com.hungts.internetbanking.model.request.TransactionRequest;
import com.hungts.internetbanking.repository.AccountRepository;
import com.hungts.internetbanking.repository.TransactionRepository;
import com.hungts.internetbanking.service.AccountService;
import com.hungts.internetbanking.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class AccountServiceImpl implements AccountService {
    @Autowired
    AccountRepository accountRepository;

    @Autowired
    UserService userService;

    @Autowired
    TransactionRepository transactionRepository;

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

    @Override
    public void payInToAccount(TransactionRequest transactionRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String phoneNumber = authentication.getName();

        UserInfo userInfo = userService.findUserByPhoneNumber(phoneNumber);
        if (userInfo == null) {
            throw new EzException("User does not exist");
        }

        Account userAccount = accountRepository.getCustomerAccountByAccountNumber(transactionRequest.getToAccountNumber());
        if (userAccount == null) {
            throw new EzException("Account does not exist");
        }

        Transaction transaction = new Transaction();
        transaction.setFromUserId(userInfo.getUserId());
        transaction.setToUserId(userAccount.getUserId());
        transaction.setFromBank(Constant.BANK_NAME);
        transaction.setToBank(Constant.BANK_NAME);
        transaction.setAmount(transactionRequest.getAmount());
        transaction.setDescription(transactionRequest.getDescription());
        transaction.setType(Constant.TransactionType.PAY_IN);
        transaction.setStatus(Constant.TransactionStatus.SUCCESS);
        transaction.setToAccountNumber(userAccount.getAccountNumber());
        transaction.setCreatedAt(new Date());
        transaction.setUpdatedAt(new Date());

        transactionRepository.saveTransaction(transaction);
        if (transaction.getId() == null) {
            throw new EzException("An error occurred while saving transaction");
        }

        long newBalance = userAccount.getBalance() + transaction.getAmount();
        accountRepository.updateAccountBalance(newBalance, userAccount.getId());
    }

    @Override
    public void transferMoney(TransactionRequest transactionRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String phoneNumber = authentication.getName();

        UserInfo userInfo = userService.findUserByPhoneNumber(phoneNumber);
        if (userInfo == null) {
            throw new EzException("User does not exist");
        }

        Account userSourceAccount = accountRepository.getCustomerAccountByAccountNumber(transactionRequest.getFromAccountNumber());
        if (userSourceAccount == null || !userSourceAccount.getUserId().equals(userInfo.getUserId())) {
            throw new EzException("Source account does not exist");
        }

        Account destinationAccount = accountRepository.getCustomerAccountByAccountNumber(transactionRequest.getToAccountNumber());
        if (destinationAccount == null) {
            throw new EzException("Destination account does not exist");
        }

        if (userSourceAccount.getBalance() < transactionRequest.getAmount()) {
            throw new EzException("Balance not enough");
        }

        Transaction transaction = new Transaction();
        transaction.setFromUserId(userInfo.getUserId());
        transaction.setToUserId(destinationAccount.getUserId());
        transaction.setFromBank(Constant.BANK_NAME);
        transaction.setToBank(Constant.BANK_NAME);
        transaction.setAmount(transactionRequest.getAmount());
        transaction.setDescription(transactionRequest.getDescription());
        transaction.setType(Constant.TransactionType.TRANSFER);
        transaction.setStatus(Constant.TransactionStatus.SUCCESS);
        transaction.setFromAccountNumber(userSourceAccount.getAccountNumber());
        transaction.setToAccountNumber(destinationAccount.getAccountNumber());
        transaction.setCreatedAt(new Date());
        transaction.setUpdatedAt(new Date());

        transactionRepository.saveTransaction(transaction);
        if (transaction.getId() == null) {
            throw new EzException("An error occurred while saving transaction");
        }

        long newBalance = userAccount.getBalance() + transaction.getAmount();
        accountRepository.updateAccountBalance(newBalance, userAccount.getId());
    }
}
