package com.hungts.internetbanking.service.impl;

import com.hungts.internetbanking.define.Constant;
import com.hungts.internetbanking.exception.EzException;
import com.hungts.internetbanking.mapper.TransactionMapper;
import com.hungts.internetbanking.model.entity.Account;
import com.hungts.internetbanking.model.entity.Transaction;
import com.hungts.internetbanking.model.info.AccountInfo;
import com.hungts.internetbanking.model.info.TransactionInfo;
import com.hungts.internetbanking.model.info.TransactionMetaData;
import com.hungts.internetbanking.model.info.UserInfo;
import com.hungts.internetbanking.model.request.AccountRequest;
import com.hungts.internetbanking.model.request.TransactionRequest;
import com.hungts.internetbanking.repository.AccountRepository;
import com.hungts.internetbanking.repository.DebtorRepository;
import com.hungts.internetbanking.repository.TransactionRepository;
import com.hungts.internetbanking.service.AccountService;
import com.hungts.internetbanking.service.UserService;
import com.hungts.internetbanking.util.EmailUtil;
import com.hungts.internetbanking.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AccountServiceImpl implements AccountService {
    @Autowired
    AccountRepository accountRepository;

    @Autowired
    UserService userService;

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    DebtorRepository debtorRepository;

    @Autowired
    TransactionMapper transactionMapper;

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

        UserInfo userInfo = userService.getUserById(account.getUserId());

        AccountInfo accountInfo = new AccountInfo(account);
        accountInfo.setAccountName(userInfo.getFullName());

        return accountInfo;
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
    public TransactionInfo createTransferMoneyTransaction(TransactionRequest transactionRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String phoneNumber = authentication.getName();

        UserInfo userInfo = userService.findUserByPhoneNumber(phoneNumber);
        if (userInfo == null) {
            throw new EzException("User does not exist");
        }

        Account userSourceAccount = accountRepository.getAccountFullInfoByAccountNumber(transactionRequest.getFromAccountNumber());
        if (userSourceAccount == null || !userSourceAccount.getUserId().equals(userInfo.getUserId())) {
            throw new EzException("Source account does not exist");
        }

        Account destinationAccount = accountRepository.getAccountFullInfoByAccountNumber(transactionRequest.getToAccountNumber());
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
        transaction.setStatus(Constant.TransactionStatus.PENDING);
        transaction.setFromAccountNumber(userSourceAccount.getAccountNumber());
        transaction.setToAccountNumber(destinationAccount.getAccountNumber());
        transaction.setOtp(Utils.randomOTP());
        transaction.setCreatedAt(new Date());
        transaction.setUpdatedAt(new Date());

        transactionRepository.saveTransaction(transaction);
        if (transaction.getId() == null) {
            throw new EzException("An error occurred while saving transaction");
        }

        try {
            EmailUtil.sendTransferOTP(userInfo, transaction.getAmount(), transaction.getOtp());
        } catch (Exception e) {
            throw new EzException("Fail to send email");
        }

        return transactionMapper.transactionToTransactionInfo(transaction);
    }

    @Override
    public void captureTransferTransaction(TransactionRequest transactionRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String phoneNumber = authentication.getName();

        UserInfo userInfo = userService.findUserByPhoneNumber(phoneNumber);
        if (userInfo == null) {
            throw new EzException("User does not exist");
        }

        Transaction transaction = transactionRepository.getUserTransactionByIdAndStatus(transactionRequest.getTransactionId(), userInfo.getUserId(), Constant.TransactionStatus.PENDING);
        if (transaction == null) {
            throw new EzException("Transaction not found");
        }

        if (System.currentTimeMillis() - transaction.getCreatedAt().getTime() > Constant.OTP_AVAILABLE_TIME) {
            transactionRepository.updateTransactionStatus(Constant.TransactionStatus.FAIL, transaction.getId());
            throw new EzException("OTP is expired");
        }

        if (!transactionRequest.getOtp().equals(transaction.getOtp())) {
            transactionRepository.updateTransactionStatus(Constant.TransactionStatus.FAIL, transaction.getId());
            throw new EzException("OTP invalid");
        }

        Account userSourceAccount = accountRepository.getAccountFullInfoByAccountNumber(transaction.getFromAccountNumber());
        if (userSourceAccount == null || !userSourceAccount.getUserId().equals(userInfo.getUserId())) {
            transactionRepository.updateTransactionStatus(Constant.TransactionStatus.FAIL, transaction.getId());
            throw new EzException("Source account does not exist");
        }

        if (userSourceAccount.getBalance() < transaction.getAmount()) {
            transactionRepository.updateTransactionStatus(Constant.TransactionStatus.FAIL, transaction.getId());
            throw new EzException("Balance not enough");
        }

        Account destinationAccount = accountRepository.getAccountFullInfoByAccountNumber(transaction.getToAccountNumber());

        long newSourceBalance = userSourceAccount.getBalance() - transaction.getAmount();
        long newDestinationBalance = destinationAccount.getBalance() + transaction.getAmount();

        accountRepository.updateAccountBalance(newSourceBalance, userSourceAccount.getId());
        accountRepository.updateAccountBalance(newDestinationBalance, destinationAccount.getId());
        transactionRepository.updateTransactionStatus(Constant.TransactionStatus.SUCCESS, transaction.getId());

        if (transaction.getDebtId() != null && transaction.getDebtId() > 0) {
            String description = "PAID - Transaction id: " + transaction.getId();
            debtorRepository.updateDebtorById(transaction.getDebtId(), description, new Date(), Constant.DebtStatus.PAID);
        }
    }

    @Override
    public TransactionMetaData getListAccountTransaction(AccountRequest accountRequest) {
        TransactionMetaData transactionMetaData = new TransactionMetaData();
        Account userAccount = accountRepository.getCustomerAccountByAccountNumber(accountRequest.getAccountNumber());

        if (userAccount == null) {
            throw new EzException("Account does not exist");
        }

        if (accountRequest.getTransactionType().equals(Constant.TransactionType.TRANSFER)) {
            List<Transaction> transactions = transactionRepository.getListTransactionByTypeFromAccount(Constant.TransactionType.TRANSFER, userAccount.getAccountNumber());
            transactionMetaData.setListTransfers(transactions.stream().map(transaction -> transactionMapper.transactionToTransactionInfo(transaction)).collect(Collectors.toCollection(LinkedList::new)));
        } else if (accountRequest.getTransactionType().equals(Constant.TransactionType.DEBT)) {
            List<Transaction> transactions = transactionRepository.getListTransactionByTypeFromAccount(Constant.TransactionType.DEBT, userAccount.getAccountNumber());
            transactionMetaData.setListDebts(transactions.stream().map(transaction -> transactionMapper.transactionToTransactionInfo(transaction)).collect(Collectors.toCollection(LinkedList::new)));
        } else if (accountRequest.getTransactionType().equals(Constant.TransactionType.PAY_IN)) {
            List<Transaction> transactions = transactionRepository.getListTransactionByTypeToAccount(userAccount.getAccountNumber());
            transactionMetaData.setListPayIn(transactions.stream().map(transaction -> transactionMapper.transactionToTransactionInfo(transaction)).collect(Collectors.toCollection(LinkedList::new)));
        } else if (accountRequest.getTransactionType().equals(Constant.TransactionType.ALL)) {
            List<Transaction> transactionsTransfers = transactionRepository.getListTransactionByTypeFromAccount(Constant.TransactionType.TRANSFER, userAccount.getAccountNumber());
            transactionMetaData.setListTransfers(transactionsTransfers.stream().map(transaction -> transactionMapper.transactionToTransactionInfo(transaction)).collect(Collectors.toCollection(LinkedList::new)));
            List<Transaction> transactionsDebts = transactionRepository.getListTransactionByTypeFromAccount(Constant.TransactionType.DEBT, userAccount.getAccountNumber());
            transactionMetaData.setListDebts(transactionsDebts.stream().map(transaction -> transactionMapper.transactionToTransactionInfo(transaction)).collect(Collectors.toCollection(LinkedList::new)));
            List<Transaction> transactionsPayIn = transactionRepository.getListTransactionByTypeToAccount(userAccount.getAccountNumber());
            transactionMetaData.setListPayIn(transactionsPayIn.stream().map(transaction -> transactionMapper.transactionToTransactionInfo(transaction)).collect(Collectors.toCollection(LinkedList::new)));
        }

        return transactionMetaData;
    }
}
