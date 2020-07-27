package com.hungts.internetbanking.service;

import com.hungts.internetbanking.model.entity.Transaction;
import com.hungts.internetbanking.model.info.*;
import com.hungts.internetbanking.model.request.AccountRequest;
import com.hungts.internetbanking.model.request.TransactionRequest;

import java.util.Date;
import java.util.List;

public interface AccountService {
    AccountInfo createAccount(AccountInfo accountRequest);

    List<AccountInfo> getAllAccountByUserId(Integer userId);

    AccountInfo getAccountInfoByAccountNumber(long accountNumber);

    void payInToAccount(TransactionRequest transactionRequest);

    TransactionInfo createTransferMoneyTransaction(TransactionRequest transactionRequest);

    void captureTransferTransaction(TransactionRequest transactionRequest);

    TransactionMetaData getListAccountTransaction(AccountRequest accountRequest);

    ResponseExternalAccountInfo getRSAAccountInfo(String bankName, Long accountNumber);

    ResponseExternalAccountInfo getPGPAccountInfo(String bankName, Long accountNumber);

    String transferMoneyToExternalBank(Transaction transaction, String accountName);

    StatisticInfo bankStatistic(Date dateFrom, Date dateTo, String bankName);
}
